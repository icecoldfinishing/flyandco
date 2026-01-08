package com.biblio.bibliotheque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api_forms")
public class ApiFormController {

    @GetMapping("/redirect_adherent")
    public String redirectToAdherentApi(@RequestParam("id") Integer id) {
        return "redirect:/api/adherent/" + id;
    }
}