package com.fly.andco.controller.compagnies;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.service.compagnies.CompagnieService;
import com.fly.andco.service.paiements.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/compagnies")
public class CompagnieController {

    @Autowired
    private CompagnieService compagnieService;

    @Autowired
    private PaiementService paiementService;

    @GetMapping
    public String listCompagnies(Model model) {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        model.addAttribute("compagnies", compagnies);
        return "views/compagnies/list";
    }

    // ===========================
    // Chiffre d'affaires par compagnie
    // ===========================
    @GetMapping("/ca")
    public String chiffreAffaire(Model model) {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        Map<Long, BigDecimal> caParCompagnie = new HashMap<>();

        for (Compagnie c : compagnies) {
            // total des paiements pour cette compagnie
            BigDecimal total = paiementService.getTotalPaiementsByCompagnie(c.getIdCompagnie());
            caParCompagnie.put(c.getIdCompagnie(), total);
        }

        model.addAttribute("compagnies", compagnies);
        model.addAttribute("caParCompagnie", caParCompagnie);

        return "views/compagnies/ca";
    }
}
