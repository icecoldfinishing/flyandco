package com.fly.andco.service.publicite;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.model.publicite.Diffusion;
import com.fly.andco.model.publicite.PaiementPublicite;
import com.fly.andco.model.publicite.Societe;
import com.fly.andco.model.publicite.TarifPublicitaire;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.publicite.DiffusionRepository;
import com.fly.andco.repository.publicite.PaiementPubliciteRepository;
import com.fly.andco.repository.publicite.TarifPublicitaireRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.dto.RevenuePublicite;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PubliciteService {
    private final DiffusionRepository diffusionRepository;
    private final TarifPublicitaireRepository tarifPublicitaireRepository;
    private final VolInstanceRepository volInstanceRepository;
    private final PaiementPubliciteRepository paiementPubliciteRepository;

    public PubliciteService(DiffusionRepository diffusionRepository, 
                            TarifPublicitaireRepository tarifPublicitaireRepository,
                            VolInstanceRepository volInstanceRepository,
                            PaiementPubliciteRepository paiementPubliciteRepository) {
        this.diffusionRepository = diffusionRepository;
        this.tarifPublicitaireRepository = tarifPublicitaireRepository;
        this.volInstanceRepository = volInstanceRepository;
        this.paiementPubliciteRepository = paiementPubliciteRepository;
    }

    public List<RevenuePublicite> getRevenueForMonth(int month, int year) {
        List<Diffusion> diffusions = diffusionRepository.findByMonthAndYear(month, year);
        return calculateRevenue(diffusions);
    }

    public List<RevenuePublicite> getRevenueForVolInstance(Integer idVolInstance) {
        VolInstance volInstance = volInstanceRepository
            .findById(idVolInstance.longValue())
            .orElseThrow(() -> new RuntimeException("VolInstance not found"));
        List<Diffusion> diffusions = diffusionRepository.findByVolInstance(volInstance);
        return calculateRevenue(diffusions);
    }

    public List<RevenuePublicite> getAllRevenueBySociete() {
        List<Diffusion> diffusions = diffusionRepository.findAll();
        return calculateRevenue(diffusions);
    }

    private List<RevenuePublicite> calculateRevenue(List<Diffusion> diffusions) {
        Map<Societe, BigDecimal> revenueMap = new HashMap<>();
        Map<Societe, Integer> countMap = new HashMap<>();

        for (Diffusion diffusion : diffusions) {
            BigDecimal montant = BigDecimal.ZERO;
            if (diffusion.getTarifPublicitaire() != null) {
                montant = diffusion.getTarifPublicitaire().getMontant() != null ? 
                          diffusion.getTarifPublicitaire().getMontant() : BigDecimal.ZERO;
            }
            
            BigDecimal revenue = montant.multiply(BigDecimal.valueOf(diffusion.getNombre()));

            revenueMap.merge(diffusion.getSociete(), revenue, BigDecimal::add);
            countMap.merge(diffusion.getSociete(), diffusion.getNombre(), Integer::sum);
        }

        List<RevenuePublicite> results = new ArrayList<>();
        for (Map.Entry<Societe, BigDecimal> entry : revenueMap.entrySet()) {
            Societe societe = entry.getKey();
            BigDecimal unitPrice = diffusions.stream()
                .filter(d -> d.getSociete().equals(societe))
                .findFirst()
                .map(d -> d.getTarifPublicitaire().getMontant())
                .orElse(BigDecimal.ZERO);

            // Fetch payments for each diffusion pertaining to this societe
            BigDecimal totalPaye = diffusions.stream()
                .filter(d -> d.getSociete().equals(societe))
                .flatMap(d -> paiementPubliciteRepository.findByDiffusion(d).stream())
                .map(PaiementPublicite::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalDu = entry.getValue();
            BigDecimal resteAPayer = totalDu.subtract(totalPaye);

            results.add(new RevenuePublicite(
                societe.getIdSociete(),
                societe.getNom(),
                countMap.get(societe),
                unitPrice,
                totalDu,
                totalPaye,
                resteAPayer
            ));
        }
        return results;
    }

    public void payerParMontant(Integer idSociete, BigDecimal montantGlobal) {
        Societe societe = new Societe();
        societe.setIdSociete(idSociete);
        List<Diffusion> diffusions = diffusionRepository.findBySociete(societe);
        
        // 1. Calculate remainders and total broadcasts of eligible (unpaid) diffusions
        Map<Diffusion, BigDecimal> remainingMap = new HashMap<>();
        int totalNombreEligible = 0;
        
        for (Diffusion diffusion : diffusions) {
            BigDecimal price = (diffusion.getTarifPublicitaire() != null) ? 
                              diffusion.getTarifPublicitaire().getMontant() : BigDecimal.ZERO;
            BigDecimal totalDu = price.multiply(BigDecimal.valueOf(diffusion.getNombre()));
            
            BigDecimal alreadyPaid = paiementPubliciteRepository.findByDiffusion(diffusion).stream()
                .map(PaiementPublicite::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal reste = totalDu.subtract(alreadyPaid);
            if (reste.compareTo(BigDecimal.ZERO) > 0) {
                remainingMap.put(diffusion, reste);
                totalNombreEligible += diffusion.getNombre();
            }
        }
        
        if (totalNombreEligible == 0 || montantGlobal.compareTo(BigDecimal.ZERO) <= 0) return;

        BigDecimal remainingToDistribute = montantGlobal;
        
        // 2. Pro-rated distribution (first pass)
        Map<Diffusion, BigDecimal> distribution = new HashMap<>();
        for (Diffusion d : remainingMap.keySet()) {
            BigDecimal share = montantGlobal.multiply(BigDecimal.valueOf(d.getNombre()))
                                            .divide(BigDecimal.valueOf(totalNombreEligible), 2, java.math.RoundingMode.DOWN);
            
            BigDecimal payment = share.min(remainingMap.get(d));
            distribution.put(d, payment);
            remainingToDistribute = remainingToDistribute.subtract(payment);
        }
        
        // 3. Surplus distribution (sequential pass for any left over due to rounding or caps)
        if (remainingToDistribute.compareTo(BigDecimal.ZERO) > 0) {
            for (Diffusion d : remainingMap.keySet()) {
                BigDecimal stillDue = remainingMap.get(d).subtract(distribution.get(d));
                if (stillDue.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal extra = stillDue.min(remainingToDistribute);
                    distribution.put(d, distribution.get(d).add(extra));
                    remainingToDistribute = remainingToDistribute.subtract(extra);
                }
                if (remainingToDistribute.compareTo(BigDecimal.ZERO) <= 0) break;
            }
        }
        
        // 4. Save payments
        for (Map.Entry<Diffusion, BigDecimal> entry : distribution.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                PaiementPublicite p = new PaiementPublicite();
                p.setDiffusion(entry.getKey());
                p.setMontant(entry.getValue());
                p.setDatePaiement(java.time.LocalDate.now());
                paiementPubliciteRepository.save(p);
            }
        }
    }
}
