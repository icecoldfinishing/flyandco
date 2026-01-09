

package com.fly.andco.controller;

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
public class FlightBookingController {

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

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping("/fix-db")
    @ResponseBody
    public String fixDatabase() {
        try {
            // 1. Clear reservation table to avoid data violation during FK creation
            try {
                jdbcTemplate.execute("TRUNCATE TABLE reservation CASCADE");
            } catch (Exception e) {
                // Ignore if table doesn't exist or other error
            }

            // 2. Fix foreign key constraint
            jdbcTemplate.execute("ALTER TABLE reservation DROP CONSTRAINT IF EXISTS reservation_id_prix_fkey");
            
            // 3. Re-add constraint pointing to correct table prix_vol
            jdbcTemplate.execute("ALTER TABLE reservation ADD CONSTRAINT reservation_id_prix_fkey FOREIGN KEY (id_prix) REFERENCES prix_vol(id_prix)");
            
            // 4. Fix foreign key constraint for vol_instance
            jdbcTemplate.execute("ALTER TABLE reservation DROP CONSTRAINT IF EXISTS reservation_id_vol_instance_fkey");
            jdbcTemplate.execute("ALTER TABLE reservation ADD CONSTRAINT reservation_id_vol_instance_fkey FOREIGN KEY (id_vol_instance) REFERENCES vol_instance(id_vol_instance)");

            return "FIXED DB SCHEMA (Constraints repaired for prix_vol and vol_instance)";
        } catch (Exception e) {
            return "Error repairing DB: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("aeroports", aeroportRepository.findAll());
        return "views/flights/search";
    }

    @GetMapping("/results")
    public String searchFlights(@RequestParam("origin") String origin,
                                @RequestParam("destination") String destination,
                                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                Model model) {
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<VolInstance> flights = volInstanceRepository.findFlights(origin, destination, startOfDay, endOfDay);
        model.addAttribute("flights", flights);
        model.addAttribute("searchDate", date);
        return "views/flights/results";
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
        return "views/flights/book";
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
        return "views/flights/confirmation";
    }
}
