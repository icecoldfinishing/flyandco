package com.fly.andco.controller.vols;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.dto.RevenueDetail;
import com.fly.andco.dto.RevenuePublicite;
import com.fly.andco.dto.TotalRevenueDTO;
import com.fly.andco.service.vols.VolService;
import com.fly.andco.service.avions.SiegeService;
import com.fly.andco.service.publicite.PubliciteService;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.repository.paiements.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
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
    private PaiementRepository paiementRepository;

    @GetMapping("/vols")
    public String listVols(Model model) {
        List<Vol> vols = volService.getAll();
        model.addAttribute("vols", vols);
        return "views/vols/list";
    }

    @GetMapping("/vols/ca-total")
    public String showTotalCa(Model model) {
        List<VolInstance> volInstances = volInstanceRepository.findAll();
        List<TotalRevenueDTO> totalRevenues = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (VolInstance vi : volInstances) {
            // Calculer le CA des tickets vendus (total, même si pas payé)
            List<RevenueDetail> ticketDetails = siegeService.calculateActualRevenue(vi.getIdVolInstance());
            BigDecimal montantTicketsVendus = BigDecimal.ZERO;
            for (RevenueDetail detail : ticketDetails) {
                montantTicketsVendus = montantTicketsVendus.add(BigDecimal.valueOf(detail.getTotal()));
            }

            // Calculer le montant payé des tickets
            BigDecimal montantTicketsPayes = paiementRepository.sumMontantByVolInstance(vi);
            if (montantTicketsPayes == null) {
                montantTicketsPayes = BigDecimal.ZERO;
            }

            // Calculer le CA de la publicité (total du)
            List<RevenuePublicite> pubRevenues = publiciteService.getRevenueForVolInstance(vi.getIdVolInstance().intValue());
            BigDecimal montantPublicite = BigDecimal.ZERO;
            for (RevenuePublicite pub : pubRevenues) {
                montantPublicite = montantPublicite.add(pub.getTotalRevenue());
            }

            // CA Total = CA tickets vendus (même si pas payé) + CA publicité
            BigDecimal montantTotal = montantTicketsVendus.add(montantPublicite);

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
                montantTicketsPayes,
                montantPublicite,
                montantTotal
            ));
        }

        model.addAttribute("totalRevenues", totalRevenues);
        return "views/vols/ca-total";
    }
}
