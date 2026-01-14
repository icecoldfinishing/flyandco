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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

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

    
    @PostMapping("/ca")
    public String filterChiffreAffaire(
            @RequestParam(required = false) Long compagnieId,
            @RequestParam(required = false) Long avionId,
            @RequestParam(required = false) Long volId,
            @RequestParam(required = false) Long volInstanceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {

        // Récupérer tous les CA
        List<ViewChiffreAffaire> caList = viewChiffreAffaireService.getAll();

        // Filtrage dynamique
        if (compagnieId != null) {
            caList = caList.stream()
                    .filter(ca -> ca.getIdCompagnie().equals(compagnieId))
                    .collect(Collectors.toList());
        }
        if (avionId != null) {
            caList = caList.stream()
                    .filter(ca -> ca.getIdAvion().equals(avionId))
                    .collect(Collectors.toList());
        }
        if (volId != null) {
            caList = caList.stream()
                    .filter(ca -> ca.getIdVol().equals(volId))
                    .collect(Collectors.toList());
        }
        if (volInstanceId != null) {
            caList = caList.stream()
                    .filter(ca -> ca.getIdVolInstance().equals(volInstanceId))
                    .collect(Collectors.toList());
        }
        if (startDate != null) {
            LocalDateTime start = startDate.atStartOfDay();
            caList = caList.stream()
                    .filter(ca -> ca.getDatePaiement() != null && !ca.getDatePaiement().isBefore(start))
                    .collect(Collectors.toList());
        }
        if (endDate != null) {
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            caList = caList.stream()
                    .filter(ca -> ca.getDatePaiement() != null && !ca.getDatePaiement().isAfter(end))
                    .collect(Collectors.toList());
        }

        // Envoyer la liste filtrée au template
        model.addAttribute("caList", caList);
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        model.addAttribute("volInstances", volInstanceRepository.findAll());

        return "views/compagnies/ca";
    }
}
