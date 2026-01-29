package com.fly.andco.controller.produit;

import com.fly.andco.model.produit.Produit;
import com.fly.andco.model.produit.VenteProduit;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.produit.ProduitRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.service.produit.VenteProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/ventes")
public class VenteProduitController {

    private final VenteProduitService venteProduitService;
    private final ProduitRepository produitRepository;
    private final VolInstanceRepository volInstanceRepository;

    public VenteProduitController(VenteProduitService venteProduitService,
                                  ProduitRepository produitRepository,
                                  VolInstanceRepository volInstanceRepository) {
        this.venteProduitService = venteProduitService;
        this.produitRepository = produitRepository;
        this.volInstanceRepository = volInstanceRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ventes", venteProduitService.getAll());
        model.addAttribute("produits", produitRepository.findAll());
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        return "views/produits/ventes";
    }

    @PostMapping
    public String add(@RequestParam("idProduit") Long idProduit,
                      @RequestParam("idVolInstance") Long idVolInstance,
                      @RequestParam("quantite") Integer quantite) {
        Produit produit = produitRepository.findById(idProduit)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));
        VolInstance vi = volInstanceRepository.findById(idVolInstance)
                .orElseThrow(() -> new IllegalArgumentException("Vol instance introuvable"));

        VenteProduit vente = new VenteProduit();
        vente.setProduit(produit);
        vente.setVolInstance(vi);
        vente.setQuantite(quantite);
        vente.setDateVente(LocalDateTime.now());

        venteProduitService.save(vente);
        return "redirect:/ventes";
    }
}
