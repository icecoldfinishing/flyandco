package com.fly.andco.controller.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.service.avions.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/avions")
public class AvionController {

    @Autowired
    private AvionService avionService;

    // Lister tous les avions
    @GetMapping
    public String listAvions(Model model) {
        List<Avion> avions = avionService.getAllAvions();
        model.addAttribute("avions", avions);
        return "views/avions/list";
    }

    // Afficher le formulaire pour créer un nouvel avion
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("avion", new Avion());
        return "views/avions/create";
    }

    // Traiter le formulaire de création d'un avion
    @PostMapping("/create")
    public String createAvion(@ModelAttribute("avion") Avion avion) {
        avionService.saveAvion(avion);
        return "redirect:/avions"; // redirige vers la liste des avions
    }
}
