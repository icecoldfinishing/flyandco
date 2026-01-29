package com.fly.andco.controller.vols;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.dto.RevenueDetail;
import com.fly.andco.dto.RevenuePublicite;
import com.fly.andco.dto.TotalRevenueDTO;
import com.fly.andco.service.vols.VolService;
import com.fly.andco.service.avions.SiegeService;
import com.fly.andco.service.publicite.PubliciteService;
import com.fly.andco.service.produit.ProduitService;
import com.fly.andco.repository.vols.VolInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VolController {

    @Autowired
    private VolService volService;

    @Autowired
    private VolInstanceRepository volInstanceRepository;

    @Autowired
    private SiegeService siegeService;

    @Autowired
    private PubliciteService publiciteService;

    @Autowired
    private ProduitService produitService;

    @GetMapping("/vols")
    public String listVols(Model model) {
        List<Vol> vols = volService.getAll();
        model.addAttribute("vols", vols);
        return "views/vols/list";
    }

    @GetMapping("/vols/ca-total")
    public String showTotalCa(Model model,
                              @RequestParam(value = "start", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                              @RequestParam(value = "end", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        LocalDateTime startDT = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endDT = (end != null) ? end.atTime(23, 59, 59) : null;

        List<VolInstance> volInstances;
        if (startDT != null || endDT != null) {
            LocalDateTime min = (startDT != null) ? startDT : LocalDateTime.of(1900, 1, 1, 0, 0);
            LocalDateTime max = (endDT != null) ? endDT : LocalDateTime.of(3000, 1, 1, 0, 0);
            volInstances = volInstanceRepository.findByDateDepartBetween(min, max);
        } else {
            volInstances = volInstanceRepository.findAll();
        }
        List<TotalRevenueDTO> totalRevenues = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (VolInstance vi : volInstances) {
            // Calculer le CA des tickets vendus via calculateActualRevenue
            List<RevenueDetail> ticketDetails = siegeService.calculateActualRevenue(vi.getIdVolInstance());
            BigDecimal montantTicketsVendus = BigDecimal.ZERO;
            for (RevenueDetail detail : ticketDetails) {
                montantTicketsVendus = montantTicketsVendus.add(BigDecimal.valueOf(detail.getTotal()));
            }

            // Calculer le CA de la publicité payée (via paiement_publicite dans calculateRevenue)
            List<RevenuePublicite> pubRevenues = publiciteService.getRevenueForVolInstance(vi.getIdVolInstance().intValue());
            BigDecimal montantPublicitePaye = BigDecimal.ZERO;
            BigDecimal montantPubliciteTotal = BigDecimal.ZERO;
            for (RevenuePublicite pub : pubRevenues) {
                montantPublicitePaye = montantPublicitePaye.add(pub.getTotalPaye());
                montantPubliciteTotal = montantPubliciteTotal.add(pub.getTotalRevenue());
            }

            // Calculer le CA des produits vendus (prix * nombre)
            BigDecimal montantProduits = produitService.getRevenueForVolInstance(vi.getIdVolInstance());

            // CA Total = CA tickets vendus + CA publicité totale (dûe) + CA produits
            BigDecimal montantTotal = montantTicketsVendus.add(montantPubliciteTotal).add(montantProduits);

            // Informations du vol
            String aeroportDepart = vi.getVol().getAeroportDepart().getVille();
            String aeroportArrivee = vi.getVol().getAeroportArrivee().getVille();
            String avion = vi.getAvion().getModele() + " (" + vi.getAvion().getNumeroImmatriculation() + ")";
            String dateDepart = vi.getDateDepart().format(formatter);

            totalRevenues.add(new TotalRevenueDTO(
                vi.getIdVolInstance(),
                aeroportDepart,
                aeroportArrivee,
                avion,
                dateDepart,
                montantTicketsVendus,
                montantPublicitePaye,
                montantPubliciteTotal,
                montantProduits,
                montantTotal
            ));
        }

        // Calculer les totaux généraux
        BigDecimal totalTicketsVendus = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantTicketsVendus)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalPublicitePaye = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantPublicite)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPubliciteTotal = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantPubliciteTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPubliciteReste = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantPubliciteReste)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProduits = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantProduits)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalGeneral = totalRevenues.stream()
            .map(TotalRevenueDTO::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalRevenues", totalRevenues);
        model.addAttribute("selectedStart", start);
        model.addAttribute("selectedEnd", end);
        model.addAttribute("totalTicketsVendus", totalTicketsVendus);
        model.addAttribute("totalPublicitePaye", totalPublicitePaye);
        model.addAttribute("totalPubliciteTotal", totalPubliciteTotal);
        model.addAttribute("totalPubliciteReste", totalPubliciteReste);
        model.addAttribute("totalProduits", totalProduits);
        model.addAttribute("totalGeneral", totalGeneral);
        return "views/vols/ca-total";
    }
}
