package com.fly.andco.controller.compagnies;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.model.compagnies.ViewChiffreAffaire;
import com.fly.andco.model.vols.VolInstance;

import com.fly.andco.service.compagnies.CompagnieService;
import com.fly.andco.service.paiements.PaiementService;
import com.fly.andco.service.compagnies.ViewChiffreAffaireService;
import com.fly.andco.service.vols.VolService;
import com.fly.andco.service.avions.AvionService;

import com.fly.andco.repository.vols.VolInstanceRepository;

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

    @Autowired
    private ViewChiffreAffaireService viewChiffreAffaireService;

    @Autowired
    private VolInstanceRepository volInstanceRepository;

    @Autowired
    private VolService volService;

    @Autowired
    private AvionService avionService;

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
        List<ViewChiffreAffaire> caList = viewChiffreAffaireService.getAll();

        // Envoyer la liste des compagnies, avions, vols et vol_instances pour les filtres
        model.addAttribute("caList", caList);
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());
        model.addAttribute("avions", avionService.getAllAvions()); // à créer si nécessaire
        model.addAttribute("vols", volService.getAll());       // à créer si nécessaire
        model.addAttribute("volInstances", volInstanceRepository.findAll()); // idem

        return "views/compagnies/ca";
    }

}
