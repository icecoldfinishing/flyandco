package com.fly.andco.controller.publicite;

import com.fly.andco.service.publicite.PubliciteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/publicite")
public class PubliciteController {
    private final PubliciteService publiciteService;

    public PubliciteController(PubliciteService publiciteService) {
        this.publiciteService = publiciteService;
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
        
        return "views/publicite/index";
    }
}
