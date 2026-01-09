package com.fly.andco.controller.vols;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.service.vols.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VolController {

    @Autowired
    private VolService volService;

    @GetMapping("/vols")
    public String listVols(Model model) {
        List<Vol> vols = volService.getAll();
        model.addAttribute("vols", vols);
        return "views/vols/list";
    }
}
