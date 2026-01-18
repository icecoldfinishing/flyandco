package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Siege;
import com.fly.andco.dto.RevenueDetail;
import com.fly.andco.repository.avions.SiegeRepository;
import com.fly.andco.repository.reservations.ReservationRepository;
import com.fly.andco.model.prix.TarifVol;
import com.fly.andco.model.reservations.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SiegeService {

    private final SiegeRepository siegeRepository;
    private final ReservationRepository reservationRepository;
    private final com.fly.andco.repository.prix.TarifVolRepository tarifVolRepository;

    @Autowired
    public SiegeService(SiegeRepository siegeRepository,
                        com.fly.andco.repository.reservations.ReservationRepository reservationRepository,
                        com.fly.andco.repository.prix.TarifVolRepository tarifVolRepository) {
        this.siegeRepository = siegeRepository;
        this.reservationRepository = reservationRepository;
        this.tarifVolRepository = tarifVolRepository;
    }

    // Lister tous les sièges
    public List<Siege> getAllSieges() {
        return siegeRepository.findAll();
    }


    public List<RevenueDetail> calculateActualRevenue(Long idVolInstance) {
        List<Reservation> reservations = reservationRepository.findByVolInstance_IdVolInstance(idVolInstance);

        // Group by (Class + PassagerType) based on ACTUAL Passenger and Seat info
        Map<String, List<Reservation>> grouped = reservations.stream()
                .collect(Collectors.groupingBy(res -> {
                    String classe = res.getSiegeVol().getSiege().getClasse();
                    String typePassager = res.getPassager().getTypePassager();
                    return classe + ":" + typePassager;
                }));

        List<RevenueDetail> details = new java.util.ArrayList<>();

        for (Map.Entry<String, List<Reservation>> entry : grouped.entrySet()) {
            List<Reservation> resList = entry.getValue();
            if (resList.isEmpty()) continue;

            // Representative reservation to get context (Class, Type)
            Reservation sample = resList.get(0);
            String classe = sample.getSiegeVol().getSiege().getClasse();
            String typePassager = sample.getPassager().getTypePassager();

            // Calculate total for this group
            BigDecimal totalGroupRevenue = BigDecimal.ZERO;

            for (Reservation res : resList) {
                // Find correct tariff for this specific reservation's flight instance, class, and passenger type
                com.fly.andco.model.prix.TarifVol correctTarif = tarifVolRepository
                        .findByVolInstance_IdVolInstanceAndClasseAndTypePassager(
                                res.getVolInstance().getIdVolInstance(),
                                classe,
                                typePassager
                        ).orElse(null);

                // If found, use its amount. If not (data inconsistency), fallback to stored tarif or 0
                if (correctTarif != null) {
                    totalGroupRevenue = totalGroupRevenue.add(correctTarif.getMontant());
                } else if (res.getTarifVol() != null) {
                    totalGroupRevenue = totalGroupRevenue.add(res.getTarifVol().getMontant());
                }
            }
            
            long count = resList.size();
            double avgPrice = count > 0 ? totalGroupRevenue.doubleValue() / count : 0.0;
            double total = totalGroupRevenue.doubleValue();

            String displayClass = classe + " (" + typePassager + ")";

            details.add(new RevenueDetail(displayClass, avgPrice, count, total));
        }

        return details;
    }
}
