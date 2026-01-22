package com.fly.andco.service.publicite;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.model.publicite.Diffusion;
import com.fly.andco.model.publicite.Societe;
import com.fly.andco.model.publicite.TarifPublicitaire;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.publicite.DiffusionRepository;
import com.fly.andco.repository.publicite.TarifPublicitaireRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
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
    private final VolInstanceRepository volInstanceRepository; // Assuming this exists or needs to be injected

    public PubliciteService(DiffusionRepository diffusionRepository, 
                            TarifPublicitaireRepository tarifPublicitaireRepository,
                            VolInstanceRepository volInstanceRepository) {
        this.diffusionRepository = diffusionRepository;
        this.tarifPublicitaireRepository = tarifPublicitaireRepository;
        this.volInstanceRepository = volInstanceRepository;
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
            Compagnie compagnie = diffusion.getVolInstance().getVol().getCompagnie();
             TarifPublicitaire tarif = tarifPublicitaireRepository.findByCompagnie(compagnie)
                 .orElse(new TarifPublicitaire());
            
            BigDecimal montant = tarif.getMontant() != null ? tarif.getMontant() : BigDecimal.ZERO;
            BigDecimal revenue = montant.multiply(BigDecimal.valueOf(diffusion.getNombre()));

            revenueMap.merge(diffusion.getSociete(), revenue, BigDecimal::add);
            countMap.merge(diffusion.getSociete(), diffusion.getNombre(), Integer::sum);
        }

        List<RevenuePublicite> results = new ArrayList<>();
        for (Map.Entry<Societe, BigDecimal> entry : revenueMap.entrySet()) {
            results.add(new RevenuePublicite(
                entry.getKey().getNom(),
                countMap.get(entry.getKey()),
                entry.getValue()
            ));
        }
        return results;
    }

    public static class RevenuePublicite {
        private String societeNom;
        private int totalDiffusions;
        private BigDecimal totalRevenue;

        public RevenuePublicite(String societeNom, int totalDiffusions, BigDecimal totalRevenue) {
            this.societeNom = societeNom;
            this.totalDiffusions = totalDiffusions;
            this.totalRevenue = totalRevenue;
        }

        public String getSocieteNom() {
            return societeNom;
        }

        public void setSocieteNom(String societeNom) {
            this.societeNom = societeNom;
        }

        public int getTotalDiffusions() {
            return totalDiffusions;
        }

        public void setTotalDiffusions(int totalDiffusions) {
            this.totalDiffusions = totalDiffusions;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }
}
