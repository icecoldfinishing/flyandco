package com.fly.andco.controller.equipages;

import com.fly.andco.model.equipages.Equipage;
import com.fly.andco.service.equipages.EquipageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EquipageController {

    @Autowired
    private EquipageService equipageService;

    @GetMapping("/equipages")
    public String listEquipages(Model model) {
        List<Equipage> equipages = equipageService.getAll();
        model.addAttribute("equipages", equipages);
        return "views/equipages/list";
    }
}
