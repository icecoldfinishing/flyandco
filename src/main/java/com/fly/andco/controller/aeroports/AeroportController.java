package com.fly.andco.controller.aeroports;

import com.fly.andco.model.aeroports.Aeroport;
import com.fly.andco.service.aeroports.AeroportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AeroportController {

    @Autowired
    private AeroportService aeroportService;

    @GetMapping("/aeroports")
    public String listAeroports(Model model) {
        List<Aeroport> aeroports = aeroportService.getAll();
        model.addAttribute("aeroports", aeroports);
        return "views/aeroports/list";
    }
}
