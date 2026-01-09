package com.fly.andco.controller.passagers;

import com.fly.andco.model.passagers.Passager;
import com.fly.andco.service.passagers.PassagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PassagerController {

    @Autowired
    private PassagerService passagerService;

    @GetMapping("/passagers")
    public String listPassagers(Model model) {
        List<Passager> passagers = passagerService.getAll();
        model.addAttribute("passagers", passagers);
        return "views/passagers/list";
    }
}
