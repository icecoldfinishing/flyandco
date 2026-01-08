package com.biblio.bibliotheque.controller.livre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.biblio.bibliotheque.service.livre.LivreService;


import com.biblio.bibliotheque.model.livre.Livre;
import com.biblio.bibliotheque.repository.livre.*;

@Controller
@RequestMapping("/livre")
public class LivreController {

    
    @Autowired
    private LivreService livreService;

    @Autowired
    private LivreRepository livreRepository;

    @GetMapping("/formapi")
    public String showFormPreterLivre(Model model) {
        model.addAttribute("livres", livreService.getAllLivres());
        return "views/formapi";
    }

    
}
