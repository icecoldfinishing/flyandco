package com.fly.andco.controller.prix;

import com.fly.andco.model.prix.TarifVol;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.prix.TarifVolRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/tarifs/vol")
public class TarifVolController {

    private final TarifVolRepository tarifVolRepository;
    private final VolInstanceRepository volInstanceRepository;

    public TarifVolController(TarifVolRepository tarifVolRepository,
                              VolInstanceRepository volInstanceRepository) {
        this.tarifVolRepository = tarifVolRepository;
        this.volInstanceRepository = volInstanceRepository;
    }

    @GetMapping("/ajouter")
    public String addForm(Model model) {
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        model.addAttribute("pageTitle", "Ajouter un tarif de vol");
        return "views/prix/tarif_vol_add";
    }

    @PostMapping("/ajouter")
    public String addTarif(@RequestParam("idVolInstance") Long idVolInstance,
                           @RequestParam("classe") String classe,
                           @RequestParam("typePassager") String typePassager,
                           @RequestParam("montant") BigDecimal montant) {
        VolInstance vi = volInstanceRepository.findById(idVolInstance).orElse(null);
        if (vi != null && montant != null) {
            TarifVol t = new TarifVol();
            t.setVolInstance(vi);
            t.setClasse(classe);
            t.setTypePassager(typePassager);
            t.setMontant(montant);
            tarifVolRepository.save(t);
        }
        return "redirect:/tarifs/vol/ajouter";
    }
}
