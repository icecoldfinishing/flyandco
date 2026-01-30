package com.fly.andco.controller.publicite;

import com.fly.andco.service.publicite.PubliciteService;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.dto.RevenuePublicite;
import com.fly.andco.model.publicite.Societe;
import com.fly.andco.model.publicite.Diffusion;
import com.fly.andco.model.publicite.TarifPublicitaire;
import com.fly.andco.repository.publicite.SocieteRepository;
import com.fly.andco.repository.publicite.DiffusionRepository;
import com.fly.andco.repository.publicite.TarifPublicitaireRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.time.LocalDate;

@Controller
@RequestMapping("/publicite")
public class PubliciteController {
    private final PubliciteService publiciteService;
    private final VolInstanceRepository volInstanceRepository;
    private final SocieteRepository societeRepository;
    private final DiffusionRepository diffusionRepository;
    private final TarifPublicitaireRepository tarifPublicitaireRepository;

    public PubliciteController(PubliciteService publiciteService,
                               VolInstanceRepository volInstanceRepository,
                               SocieteRepository societeRepository,
                               DiffusionRepository diffusionRepository,
                               TarifPublicitaireRepository tarifPublicitaireRepository) {
        this.publiciteService = publiciteService;
        this.volInstanceRepository = volInstanceRepository;
        this.societeRepository = societeRepository;
        this.diffusionRepository = diffusionRepository;
        this.tarifPublicitaireRepository = tarifPublicitaireRepository;
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

    // ========= Gestion Sociétés =========
    @GetMapping("/societes/ajouter")
    public String ajouterSocieteForm(Model model) {
        model.addAttribute("pageTitle", "Ajouter une société");
        model.addAttribute("societes", societeRepository.findAll());
        return "views/publicite/societes_add";
    }

    @PostMapping("/societes/ajouter")
    public String ajouterSociete(@RequestParam("nom") String nom) {
        if (nom != null && !nom.trim().isEmpty()) {
            Societe s = new Societe();
            s.setNom(nom.trim());
            societeRepository.save(s);
        }
        return "redirect:/publicite/societes/ajouter";
    }

    // ========= Gestion Diffusions =========
    @GetMapping("/diffusions")
    public String diffusions(Model model) {
        model.addAttribute("pageTitle", "Diffusions publicitaires");
        model.addAttribute("diffusions", diffusionRepository.findAll());
        model.addAttribute("societes", societeRepository.findAll());
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        model.addAttribute("tarifs", tarifPublicitaireRepository.findAll());
        return "views/publicite/diffusions";
    }

    @PostMapping("/diffusions")
    public String createDiffusion(@RequestParam("idSociete") Integer idSociete,
                                  @RequestParam("idVolInstance") Long idVolInstance,
                                  @RequestParam("idTarifPub") Integer idTarifPub,
                                  @RequestParam("dateDiffusion") String dateDiffusion,
                                  @RequestParam("nombre") Integer nombre) {
        Societe societe = societeRepository.findById(idSociete).orElse(null);
        VolInstance volInstance = volInstanceRepository.findById(idVolInstance).orElse(null);
        TarifPublicitaire tarif = tarifPublicitaireRepository.findById(idTarifPub).orElse(null);
        if (societe != null && volInstance != null && tarif != null && nombre != null && nombre >= 0) {
            Diffusion d = new Diffusion();
            d.setSociete(societe);
            d.setVolInstance(volInstance);
            d.setTarifPublicitaire(tarif);
            d.setNombre(nombre);
            try {
                d.setDateDiffusion(LocalDate.parse(dateDiffusion));
            } catch (Exception e) {
                d.setDateDiffusion(LocalDate.now());
            }
            diffusionRepository.save(d);
        }
        return "redirect:/publicite/diffusions";
    }
}
