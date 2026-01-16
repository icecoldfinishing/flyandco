package com.fly.andco.controller.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.dto.RevenueDetail;
import com.fly.andco.service.avions.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String showRevenueForm(Model model) { 
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        return "views/avions/place";
    }

    @PostMapping("/revenue")
    public String calculateRevenue(@RequestParam("idVol") Long idVol,
                                   Model model) {
        List<RevenueDetail> details = siegeService.calculateMaxRevenue(idVol);
        
        double grandTotal = details.stream().mapToDouble(RevenueDetail::getTotal).sum();

        model.addAttribute("revenueDetails", details);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("vols", volService.getAll());
        model.addAttribute("selectedVolId", idVol);
        return "views/avions/place";
    }
    @GetMapping("/ca")
    public String showCaForm(Model model) { 
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        return "views/avions/ca";
    }
}
