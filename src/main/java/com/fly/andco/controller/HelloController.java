package com.fly.andco.controller;

import com.fly.andco.model.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class HelloController {

    @GetMapping("/bienvenue")
    public String afficherHello(Model model) {
        model.addAttribute("message", "Bienvenue !");
        return "views/hello";
    }

    @GetMapping("/hello")
    public String hello(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("user");
        if (utilisateur == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", utilisateur.getUsername());
        model.addAttribute("role", utilisateur.getRole());
        return "views/hello";
    }
}
