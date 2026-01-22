package com.fly.andco.controller.publicite;

import com.fly.andco.service.publicite.PubliciteService;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.vols.VolInstanceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        List<PubliciteService.RevenuePublicite> revenues = publiciteService.getRevenueForMonth(month, year);
        
        // Calculate total global revenue
        java.math.BigDecimal totalGlobal = revenues.stream()
            .map(PubliciteService.RevenuePublicite::getTotalRevenue)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);


        model.addAttribute("revenues", revenues);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("totalGlobal", totalGlobal);
        model.addAttribute("pageTitle", "Revenus Publicitaires - " + month + "/" + year);
        
        return "views/publicite/index";
    }

    @GetMapping("/vol")
    public String revenueByVol(@RequestParam("id") Integer idVolInstance, Model model) {
        VolInstance volInstance = volInstanceRepository
            .findById(idVolInstance.longValue())
            .orElse(null);

        List<PubliciteService.RevenuePublicite> revenues = publiciteService.getRevenueForVolInstance(idVolInstance);

        java.math.BigDecimal totalGlobal = revenues.stream()
            .map(PubliciteService.RevenuePublicite::getTotalRevenue)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("revenues", revenues);
        model.addAttribute("totalGlobal", totalGlobal);
        model.addAttribute("pageTitle", "Revenus Publicitaires - Vol " + volInstance.getVol().getCompagnie().getNom() + " (" + volInstance.getDateDepart().toString() + ")");
        // Hide filters for this view or adjust them
        model.addAttribute("hideFilters", true); 

        return "views/publicite/index";
    }
}
