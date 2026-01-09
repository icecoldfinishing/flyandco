package com.fly.andco.controller.pilotes;

import com.fly.andco.model.pilotes.Pilote;
import com.fly.andco.service.pilotes.PiloteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PiloteController {

    @Autowired
    private PiloteService piloteService;

    @GetMapping("/pilotes")
    public String listPilotes(Model model) {
        List<Pilote> pilotes = piloteService.getAll();
        model.addAttribute("pilotes", pilotes);
        return "views/pilotes/list";
    }
}
