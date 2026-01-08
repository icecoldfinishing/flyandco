package com.biblio.bibliotheque.controller.reservation;

import com.biblio.bibliotheque.model.livre.Livre;
import com.biblio.bibliotheque.model.reservation.Reservation;
import com.biblio.bibliotheque.model.reservation.StatutReservation;
import com.biblio.bibliotheque.repository.livre.LivreRepository;
import com.biblio.bibliotheque.repository.reservation.ReservationRepository;
import com.biblio.bibliotheque.repository.reservation.StatutReservationRepository;
import com.biblio.bibliotheque.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StatutReservationRepository statutReservationRepository;

    @GetMapping("/add")
    public String showReservationForm(Model model) {
        List<Livre> livres = livreRepository.findAll();
        model.addAttribute("livres", livres);
        return "views/reservation/add";
    }

    @PostMapping("/add")
    public String addReservation(@RequestParam("idLivre") Integer idLivre,
                                 @RequestParam("dateReservation") Date dateReservation,
                                 RedirectAttributes redirectAttributes) {
        try {
            // For now, we'll use a hardcoded adherent ID.
            // In a real application, you would get this from the session.
            Integer idAdherent = 1;

            reservationService.creerReservation(idLivre, idAdherent, dateReservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation créée avec succès!");
            return "redirect:/reservation/list";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reservation/add";
        }
    }

    @GetMapping("/list")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservations", reservations);
        return "views/reservation/list";
    }

    @PostMapping("/validate")
    public String validateReservation(@RequestParam("idReservation") Integer idReservation,
                                      RedirectAttributes redirectAttributes) {
        try {
            reservationService.validerReservation(idReservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation validée avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reservation/list";
    }

    @PostMapping("/reject")
    public String rejectReservation(@RequestParam("idReservation") Integer idReservation,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.rejectReservation(idReservation);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation rejetée avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/librarian/reservation/list";
    }
}

