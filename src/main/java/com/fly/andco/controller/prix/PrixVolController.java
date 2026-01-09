package com.fly.andco.controller.prix;

import com.fly.andco.model.prix.PrixVol;
import com.fly.andco.service.prix.PrixVolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PrixVolController {

    @Autowired
    private PrixVolService prixVolService;

    @GetMapping("/prix-vols")
    public String listPrixVols(Model model) {
        List<PrixVol> prixVols = prixVolService.getAll();
        model.addAttribute("prixVols", prixVols);
        return "views/prix/list";
    }
}
