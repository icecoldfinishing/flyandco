package com.biblio.bibliotheque.controller.livre;

import com.biblio.bibliotheque.service.livre.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/livre_forms")
public class LivreFormController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/livre")
    public String showLivreApiForm(Model model) {
        model.addAttribute("livres", livreService.getAllLivres());
        return "views/formapi";
    }

    @PostMapping("/details")
    public String redirectToLivreDetails(@RequestParam("idLivre") Integer idLivre) {
        return "redirect:/api/livre/" + idLivre;
    }
}