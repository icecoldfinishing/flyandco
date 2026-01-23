package com.fly.andco.controller.publicite;

import com.fly.andco.service.publicite.PubliciteService;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.dto.RevenuePublicite;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/publicite")
public class PubliciteController {
    private final PubliciteService publiciteService;
    private final VolInstanceRepository volInstanceRepository;

    public PubliciteController(PubliciteService publiciteService, VolInstanceRepository volInstanceRepository) {
        this.publiciteService = publiciteService;
        this.volInstanceRepository = volInstanceRepository;
    }

    @GetMapping
    public String index(@RequestParam(required = false) Integer month,
                        @RequestParam(required = false) Integer year,
                        Model model) {
        if (month == null) {
            month = 12; // Default to Dec for the test case
        }
        if (year == null) {
            year = 2025; // Default to 2025 for the test case
        }

        List<RevenuePublicite> revenues = publiciteService.getRevenueForMonth(month, year);
        
        // Calculate global totals
        java.math.BigDecimal totalGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalRevenue)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalPayeGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalPaye)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalResteGlobal = revenues.stream()
            .map(RevenuePublicite::getResteAPayer)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);


        model.addAttribute("revenues", revenues);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("totalGlobal", totalGlobal);
        model.addAttribute("totalPayeGlobal", totalPayeGlobal);
        model.addAttribute("totalResteGlobal", totalResteGlobal);
        model.addAttribute("pageTitle", "Revenus Publicitaires - " + month + "/" + year);
        
        return "views/publicite/index";
    }

    @GetMapping("/vol")
    public String revenueByVol(@RequestParam("id") Integer idVolInstance, Model model) {
        VolInstance volInstance = volInstanceRepository
            .findById(idVolInstance.longValue())
            .orElse(null);

        List<RevenuePublicite> revenues = publiciteService.getRevenueForVolInstance(idVolInstance);

        java.math.BigDecimal totalGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalRevenue)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalPayeGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalPaye)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalResteGlobal = revenues.stream()
            .map(RevenuePublicite::getResteAPayer)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("revenues", revenues);
        model.addAttribute("totalGlobal", totalGlobal);
        model.addAttribute("totalPayeGlobal", totalPayeGlobal);
        model.addAttribute("totalResteGlobal", totalResteGlobal);
        model.addAttribute("pageTitle", "Revenus Publicitaires - Vol " + (volInstance != null ? volInstance.getVol().getCompagnie().getNom() : "") + " (" + (volInstance != null ? volInstance.getDateDepart().toString() : "") + ")");
        model.addAttribute("hideFilters", true); 

        return "views/publicite/index";
    }

    @GetMapping("/societes")
    public String revenueBySociete(Model model) {
        List<RevenuePublicite> revenues = publiciteService.getAllRevenueBySociete();

        java.math.BigDecimal totalGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalRevenue)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalPayeGlobal = revenues.stream()
            .map(RevenuePublicite::getTotalPaye)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalResteGlobal = revenues.stream()
            .map(RevenuePublicite::getResteAPayer)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("revenues", revenues);
        model.addAttribute("totalGlobal", totalGlobal);
        model.addAttribute("totalPayeGlobal", totalPayeGlobal);
        model.addAttribute("totalResteGlobal", totalResteGlobal);
        model.addAttribute("pageTitle", "Revenus Publicitaires par Société");
        model.addAttribute("isSocieteView", true);
        model.addAttribute("hideFilters", true);

        return "views/publicite/societes";
    }

    @GetMapping("/paiement/{idSociete}")
    public String preparationPaiement(@PathVariable Integer idSociete, Model model) {
        List<RevenuePublicite> revenues = publiciteService.getAllRevenueBySociete();
        RevenuePublicite societeRevenue = revenues.stream()
            .filter(r -> r.getIdSociete().equals(idSociete))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Société not found"));

        model.addAttribute("revenue", societeRevenue);
        model.addAttribute("pageTitle", "Paiement Intégral - " + societeRevenue.getSocieteNom());
        
        return "views/publicite/paiement_societe";
    }

    @PostMapping("/paiement")
    public String effectuerPaiement(@RequestParam Integer idSociete, @RequestParam java.math.BigDecimal montant) {
        publiciteService.payerParMontant(idSociete, montant);
        return "redirect:/publicite/societes";
    }
}
