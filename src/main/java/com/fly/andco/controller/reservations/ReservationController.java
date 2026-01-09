package com.fly.andco.controller.reservations;

import com.fly.andco.model.reservations.Reservation;
import com.fly.andco.service.reservations.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAll();
        model.addAttribute("reservations", reservations);
        return "views/reservations/list";
    }
}
