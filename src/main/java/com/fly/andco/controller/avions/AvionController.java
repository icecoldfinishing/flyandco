package com.fly.andco.controller.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.service.avions.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/avions")
public class AvionController {

    @Autowired
    private AvionService avionService;

    // Lister tous les avions
    @GetMapping
    public String listAvions(Model model) {
        List<Avion> avions = avionService.getAllAvions();
        model.addAttribute("avions", avions);
        return "views/avions/list";
    }

    @Autowired
    private com.fly.andco.service.avions.SiegeService siegeService;

    @Autowired
    private com.fly.andco.service.vols.VolService volService;

    @GetMapping("/place")
    public String showRevenueForm(Model model) { // Modified to serve as the revenue calc page as requested
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        return "views/avions/place";
    }

    @PostMapping("/revenue")
    public String calculateRevenue(@org.springframework.web.bind.annotation.RequestParam("idAvion") Long idAvion,
                                   @org.springframework.web.bind.annotation.RequestParam("idVol") Long idVol,
                                   Model model) {
        Double revenue = siegeService.calculateMaxRevenue(idAvion, idVol);
        model.addAttribute("revenue", revenue);
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        model.addAttribute("selectedAvionId", idAvion);
        model.addAttribute("selectedVolId", idVol);
        return "views/avions/place";
    }
}
