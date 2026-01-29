package com.fly.andco.controller.produit;

import com.fly.andco.model.produit.Produit;
import com.fly.andco.model.publicite.Societe;
import com.fly.andco.repository.publicite.SocieteRepository;
import com.fly.andco.service.produit.ProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;
    private final SocieteRepository societeRepository;

    public ProduitController(ProduitService produitService, SocieteRepository societeRepository) {
        this.produitService = produitService;
        this.societeRepository = societeRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("produits", produitService.getAll());
        model.addAttribute("societes", societeRepository.findAll());
        model.addAttribute("newProduit", new Produit());
        return "views/produits/index";
    }

    @PostMapping
    public String add(@ModelAttribute("newProduit") Produit produit,
                      @RequestParam("societeId") Integer societeId,
                      @RequestParam("nom") String nom,
                      @RequestParam("prix") BigDecimal prix,
                      @RequestParam(value = "description", required = false) String description,
                      @RequestParam(value = "disponible", required = false) Boolean disponible,
                      BindingResult bindingResult,
                      Model model) {

        Societe societe = societeRepository.findById(societeId)
                .orElseThrow(() -> new IllegalArgumentException("Société introuvable"));

        Produit p = new Produit();
        p.setSociete(societe);
        p.setNom(nom);
        p.setPrix(prix);
        p.setDescription(description);
        p.setDisponible(disponible != null ? disponible : Boolean.TRUE);
        p.setDateAjout(LocalDate.now());

        produitService.save(p);
        return "redirect:/produits";
    }
}
