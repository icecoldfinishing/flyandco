package com.fly.andco.controller.reservation;

import com.fly.andco.model.reservations.Reservation;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.model.passagers.Passager;
import com.fly.andco.model.prix.PrixVol;
import com.fly.andco.repository.vols.VolRepository;
import com.fly.andco.repository.VolInstanceRepository;
import com.fly.andco.repository.aeroports.AeroportRepository;
import com.fly.andco.repository.reservations.ReservationRepository;
import com.fly.andco.repository.passagers.PassagerRepository;
import com.fly.andco.repository.prix.PrixVolRepository;
import com.fly.andco.repository.passagers.PassagerRepository;
import com.fly.andco.service.reservations.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/flights")
public class ReservationController {

    
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VolInstanceRepository volInstanceRepository;
    
    @Autowired
    private AeroportRepository aeroportRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassagerRepository passagerRepository;
    
    @Autowired
    private PrixVolRepository prixVolRepository;


    @GetMapping("/list")
    public String listReservationss(Model model) {
        List<Reservation> reservations = reservationService.getAll();
        model.addAttribute("reservations", reservations);
        model.addAttribute("passagers", passagerRepository.findAll());
        return "views/reservation/list";
    }
    @GetMapping("/list/results")
    public String searchReservations(@RequestParam("passagerId") Long passagerId,
                                    @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    Model model) {

        List<Reservation> reservations;

        if (startDate != null && endDate != null) {
            // On filtre entre les dates
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
            reservations = reservationRepository.findByPassager_IdPassagerAndDateReservationBetween(passagerId, startOfDay, endOfDay);
        } else {
            // On récupère juste toutes les réservations pour le passager
            reservations = reservationRepository.findByPassager_IdPassager(passagerId);
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("passagers", passagerRepository.findAll());
        model.addAttribute("passagerId", passagerId);
        model.addAttribute("searchStart", startDate);
        model.addAttribute("searchEnd", endDate);

        return "views/reservation/list";
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("aeroports", aeroportRepository.findAll());
        List<VolInstance> flights = volInstanceRepository.findAll();
        model.addAttribute("flights", flights);
        return "views/reservation/search";
    }

    @GetMapping("/results")
    public String searchFlights(@RequestParam("origin") String origin,
                                @RequestParam("destination") String destination,
                                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                Model model) {
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<VolInstance> flights = volInstanceRepository.findFlights(origin, destination, startOfDay, endOfDay);
        model.addAttribute("aeroports", aeroportRepository.findAll());
        model.addAttribute("flights", flights);
        model.addAttribute("searchDate", date);
        return "views/reservation/search";
    }

    @GetMapping("/book/{id}")
    public String showBookingForm(@PathVariable("id") Long id, Model model) {
        VolInstance vol = volInstanceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid flight Id:" + id));
        List<PrixVol> prices = prixVolRepository.findAll(); // Should filter by vol actually
        // filtering locally for now or add method in repo
        // Ideally: prixVolRepository.findByVol(vol.getVol());
        
        model.addAttribute("flight", vol);
        // Simple hack to get prices for this flight's route
        model.addAttribute("prices", prices.stream().filter(p -> p.getVol().getIdVol().equals(vol.getVol().getIdVol())).toList());
        model.addAttribute("passenger", new Passager());
        return "views/reservation/book";
    }

    @PostMapping("/purchase")
    public String purchaseTicket(@RequestParam("flightId") Long flightId,
                                    @RequestParam("priceId") Long priceId,
                                    @ModelAttribute Passager passenger,
                                    Model model) {
        
        // Save passenger if not exists (simplified logic)
        Passager savedPassenger = passagerRepository.save(passenger);
        
        VolInstance flight = volInstanceRepository.findById(flightId).get();
        PrixVol price = prixVolRepository.findById(priceId).get();
        
        Reservation reservation = new Reservation();
        reservation.setPassager(savedPassenger);
        reservation.setVolInstance(flight);
        reservation.setPrixVol(price);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("CONFIRMEE");
        
        reservationRepository.save(reservation);
        
        model.addAttribute("reservation", reservation);
        return "views/reservation/confirmation";
    }
}
