package com.fly.andco.controller.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.service.avions.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class AvionController {

    @Autowired
    private AvionService avionService;

    @GetMapping("/avions")
    public String listAvions(Model model) {
        List<Avion> avions = avionService.getAllAvions();
        model.addAttribute("avions", avions);
        return "views/avions/list";
    }
}
