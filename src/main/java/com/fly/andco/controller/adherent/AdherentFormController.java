package com.biblio.bibliotheque.controller.adherent;

import com.biblio.bibliotheque.service.gestion.AdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/adherent_forms")
public class AdherentFormController {

    @Autowired
    private AdherentService adherentService;

    @GetMapping("/adherent")
    public String showAdherentApiForm(Model model) {
        model.addAttribute("adherents", adherentService.getAll());
        return "views/api_form";
    }

    @PostMapping("/details")
    public String redirectToAdherentDetails(@RequestParam("id") Integer idAdherent) {
        return "redirect:/api/adherent/" + idAdherent;
    }
}