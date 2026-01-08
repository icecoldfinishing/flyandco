
package com.biblio.bibliotheque.controller.librarian;

import com.biblio.bibliotheque.model.reservation.Reservation;
import com.biblio.bibliotheque.repository.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/librarian/reservation")
public class LibrarianReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/list")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAllWithStatutReservation();
        model.addAttribute("reservations", reservations);
        return "librarian/reservation/list";
    }
}
