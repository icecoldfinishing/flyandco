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
}
