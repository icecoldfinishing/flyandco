package com.fly.andco.controller.compagnies;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.service.compagnies.CompagnieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CompagnieController {
    @Autowired
    private CompagnieService compagnieService;

    @GetMapping("/compagnies")
    public String listCompagnies(Model model) {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        model.addAttribute("compagnies", compagnies);
        return "views/compagnies/list";
    }
}
