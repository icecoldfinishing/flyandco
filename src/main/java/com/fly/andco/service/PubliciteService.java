package com.fly.andco.service;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.model.Diffusion;
import com.fly.andco.model.Societe;
import com.fly.andco.model.TarifPublicitaire;
import com.fly.andco.repository.DiffusionRepository;
import com.fly.andco.repository.TarifPublicitaireRepository;
import lombok.Data;
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

    public PubliciteService(DiffusionRepository diffusionRepository, TarifPublicitaireRepository tarifPublicitaireRepository) {
        this.diffusionRepository = diffusionRepository;
        this.tarifPublicitaireRepository = tarifPublicitaireRepository;
    }

    public List<RevenuePublicite> getRevenueForMonth(int month, int year) {
        List<Diffusion> diffusions = diffusionRepository.findByMonthAndYear(month, year);
        Map<Societe, BigDecimal> revenueMap = new HashMap<>();
        Map<Societe, Integer> countMap = new HashMap<>();

        for (Diffusion diffusion : diffusions) {
            Compagnie compagnie = diffusion.getVolInstance().getVol().getCompagnie();
             TarifPublicitaire tarif = tarifPublicitaireRepository.findByCompagnie(compagnie)
                 .orElse(new TarifPublicitaire()); // Or handle missing tarif appropriately
            
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

    @Data
    public static class RevenuePublicite {
        private String societeNom;
        private int totalDiffusions;
        private BigDecimal totalRevenue;

        public RevenuePublicite(String societeNom, int totalDiffusions, BigDecimal totalRevenue) {
            this.societeNom = societeNom;
            this.totalDiffusions = totalDiffusions;
            this.totalRevenue = totalRevenue;
        }
    }
}
