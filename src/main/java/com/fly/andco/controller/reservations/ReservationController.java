package com.fly.andco.controller.reservations;

import com.fly.andco.model.reservations.Reservation;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.model.passagers.Passager;
import com.fly.andco.model.paiements.*;

import com.fly.andco.repository.vols.VolRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
import com.fly.andco.repository.aeroports.AeroportRepository;
import com.fly.andco.repository.reservations.ReservationRepository;
import com.fly.andco.repository.passagers.PassagerRepository;
import com.fly.andco.repository.paiements.MoyenPaiementRepository;
import com.fly.andco.repository.paiements.PaiementRepository;
import com.fly.andco.service.reservations.ReservationService;
import com.fly.andco.repository.prix.TarifVolRepository;
import com.fly.andco.repository.vols.SiegeVolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.math.BigDecimal;

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
    private TarifVolRepository tarifVolRepository;

    @Autowired
    private SiegeVolRepository siegeVolRepository;

    @Autowired
    private MoyenPaiementRepository moyenPaiementRepository;

    @Autowired
    private PaiementRepository paiementRepository;

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
        VolInstance vol = volInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight Id:" + id));

        List<com.fly.andco.model.prix.TarifVol> tarifs = tarifVolRepository.findByVolInstance_IdVolInstance(id);
        List<MoyenPaiement> moyensPaiement = moyenPaiementRepository.findAll();

        model.addAttribute("flight", vol);
        model.addAttribute("prices", tarifs);
        model.addAttribute("moyensPaiement", moyensPaiement);
        model.addAttribute("passenger", new Passager());

        return "views/reservation/book";
    }


    @PostMapping("/purchase")
    public String purchaseTicket(
            @RequestParam("flightId") Long flightId,
            @RequestParam("priceId") Long tarifId,
            @RequestParam("moyenPaiementId") Long moyenPaiementId,
            @ModelAttribute Passager passenger,
            Model model) {
        
        Passager savedPassenger = passagerRepository.save(passenger);
        
        VolInstance flight = volInstanceRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));

        com.fly.andco.model.prix.TarifVol tarif = tarifVolRepository.findById(tarifId)
                .orElseThrow(() -> new IllegalArgumentException("Tarif introuvable"));

        // Auto-assign Seat
        List<com.fly.andco.model.vols.SiegeVol> siegeVols = siegeVolRepository.findByVolInstance_IdVolInstanceAndStatut(flightId, "LIBRE");
        
        // Filter by class matching the tarif
        com.fly.andco.model.vols.SiegeVol selectedSiege = siegeVols.stream()
            .filter(sv -> sv.getSiege().getClasse().equalsIgnoreCase(tarif.getClasse()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Aucun siège disponible pour la classe " + tarif.getClasse()));

        // Mark seat determined
        selectedSiege.setStatut("OCCUPE");
        siegeVolRepository.save(selectedSiege);

        Reservation reservation = new Reservation();
        reservation.setPassager(savedPassenger);
        reservation.setVolInstance(flight);
        reservation.setTarifVol(tarif);
        reservation.setSiegeVol(selectedSiege);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("CONFIRMEE");

        reservation = reservationRepository.save(reservation);

        MoyenPaiement moyenPaiement = moyenPaiementRepository.findById(moyenPaiementId)
                .orElseThrow(() -> new IllegalArgumentException("Moyen de paiement introuvable"));

        Paiement paiement = new Paiement();
        paiement.setReservation(reservation);
        paiement.setMontant(tarif.getMontant());
        paiement.setMoyenPaiement(moyenPaiement);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setStatut("OK");

        paiementRepository.save(paiement);

        model.addAttribute("reservation", reservation);
        model.addAttribute("paiement", paiement);

        return "views/reservation/confirmation";
    }

}
