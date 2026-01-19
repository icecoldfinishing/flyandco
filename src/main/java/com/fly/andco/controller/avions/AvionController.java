    // Endpoint AJAX pour fragment chiffre d'affaires
    
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

    @Autowired
    private com.fly.andco.repository.vols.VolInstanceRepository volInstanceRepository;



    @PostMapping("/revenue")
    public String calculateRevenue(@RequestParam("idVolInstance") Long idVolInstance,
                                   Model model) {
        List<RevenueDetail> details = siegeService.calculateActualRevenue(idVolInstance);
        
        double grandTotal = details.stream().mapToDouble(RevenueDetail::getTotal).sum();

        model.addAttribute("revenueDetails", details);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("vols", volService.getAll());
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        model.addAttribute("selectedVolInstanceId", idVolInstance);
        return "views/avions/ca";
    }
    @PostMapping("/revenue/fragment")
    public String revenueFragment(@RequestParam("idVolInstance") Long idVolInstance, Model model) {
        List<RevenueDetail> details = siegeService.calculateActualRevenue(idVolInstance);
        double grandTotal = details.stream().mapToDouble(RevenueDetail::getTotal).sum();
        model.addAttribute("revenueDetails", details);
        model.addAttribute("grandTotal", grandTotal);
        return "views/avions/ca :: revenueTable";
    }
    @GetMapping("/ca")
    public String showCaForm(Model model) { 
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("vols", volService.getAll());
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        return "views/avions/ca";
    }

    @GetMapping("/placeSimulation")
    public String showSimulationForm(Model model) {
        if (!model.containsAttribute("simulationRequest")) {
            com.fly.andco.dto.SimulationRequest request = new com.fly.andco.dto.SimulationRequest();
            // Pre-populate with some default rows for better UX
            request.getItems().add(new com.fly.andco.dto.SimulationRequest.SimulationItem());
            model.addAttribute("simulationRequest", request);
        }
        return "views/avions/place_test";
    }

    @PostMapping("/simulation")
    public String calculateSimulation(@ModelAttribute com.fly.andco.dto.SimulationRequest request, Model model) {
        double grandTotal = 0.0;
        if (request.getItems() != null) {
            for (com.fly.andco.dto.SimulationRequest.SimulationItem item : request.getItems()) {
                 grandTotal += item.getTotal();
            }
        }
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("simulationRequest", request);
        return "views/avions/place_test";
    }
}
