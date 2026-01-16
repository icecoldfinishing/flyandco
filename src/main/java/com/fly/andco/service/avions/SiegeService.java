package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Siege;
import com.fly.andco.dto.RevenueDetail;
import com.fly.andco.repository.avions.SiegeRepository;
import com.fly.andco.repository.prix.PrixVolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiegeService {

    private final SiegeRepository siegeRepository;

    private final PrixVolRepository prixVolRepository;

    private final com.fly.andco.repository.reservations.ReservationRepository reservationRepository;
    private final com.fly.andco.repository.prix.PromotionRepository promotionRepository;

    @Autowired
    public SiegeService(SiegeRepository siegeRepository, 
                        PrixVolRepository prixVolRepository,
                        com.fly.andco.repository.reservations.ReservationRepository reservationRepository,
                        com.fly.andco.repository.prix.PromotionRepository promotionRepository) {
        this.siegeRepository = siegeRepository;
        this.prixVolRepository = prixVolRepository;
        this.reservationRepository = reservationRepository;
        this.promotionRepository = promotionRepository;
    }

    // Lister tous les sièges
    public List<Siege> getAllSieges() {
        return siegeRepository.findAll();
    }

    public List<RevenueDetail> calculateMaxRevenue(Long volId) {
        // Fetch seats for the specific vol (now that Siege is linked to Vol)
        List<Siege> sieges = siegeRepository.findByVol_IdVol(volId);
        
        List<com.fly.andco.model.prix.PrixVol> prixVols = prixVolRepository.findAll(); 

        // Filter prices for the specific Vol
        List<com.fly.andco.model.prix.PrixVol> prixForVol = prixVols.stream()
                .filter(p -> p.getVol().getIdVol().equals(volId))
                .toList();

        // Group seats by class
        java.util.Map<String, Long> seatsByClass = sieges.stream()
                .collect(java.util.stream.Collectors.groupingBy(Siege::getClasse, java.util.stream.Collectors.counting()));

        java.util.List<com.fly.andco.dto.RevenueDetail> details = new java.util.ArrayList<>();

        for (java.util.Map.Entry<String, Long> entry : seatsByClass.entrySet()) {
            String classe = entry.getKey();
            Long count = entry.getValue();

            // Find price for this class
            Optional<com.fly.andco.model.prix.PrixVol> prixOpt = prixForVol.stream()
                    .filter(p -> p.getClasse().equalsIgnoreCase(classe))
                    .findFirst();

            double price = prixOpt.map(com.fly.andco.model.prix.PrixVol::getPrix).orElse(0.0);
            double total = count * price;

            details.add(new com.fly.andco.dto.RevenueDetail(classe, price, count, total));
        }

        return details;
    }

    // Lister les sièges d’un vol
    public List<Siege> getSiegesByVol(Long volId) {
        return siegeRepository.findByVol_IdVol(volId);
    }


    public List<RevenueDetail> calculateActualRevenue(Long volId) {
        List<com.fly.andco.model.reservations.Reservation> reservations = reservationRepository.findByVolInstance_Vol_IdVol(volId);
        List<com.fly.andco.model.prix.Promotion> allPromotions = promotionRepository.findAll();

        java.util.Map<String, List<com.fly.andco.model.reservations.Reservation>> grouped = reservations.stream()
                .collect(java.util.stream.Collectors.groupingBy(res -> {
                    String classe = res.getPrixVol().getClasse();
                    String typePassager = res.getPassager().getTypePassager();
                    return classe + ":" + typePassager;
                }));

        java.util.List<RevenueDetail> details = new java.util.ArrayList<>();

        for (java.util.Map.Entry<String, List<com.fly.andco.model.reservations.Reservation>> entry : grouped.entrySet()) {
            List<com.fly.andco.model.reservations.Reservation> resList = entry.getValue();
            if (resList.isEmpty()) continue;

            com.fly.andco.model.reservations.Reservation sample = resList.get(0);
            com.fly.andco.model.prix.PrixVol prixVol = sample.getPrixVol();
            String typePassager = sample.getPassager().getTypePassager();
            String classeName = prixVol.getClasse();

            double basePrice = prixVol.getPrix();

            Optional<com.fly.andco.model.prix.Promotion> promoOpt = allPromotions.stream()
                    .filter(p -> p.getPrixVol().getIdPrix().equals(prixVol.getIdPrix()) 
                              && p.getTypePassager().equalsIgnoreCase(typePassager))
                    .findFirst();

            double finalPrice;
            if (promoOpt.isPresent()) {
                finalPrice = promoOpt.get().getMontant();
            } else {
                finalPrice = basePrice;
            }

            String displayClass = classeName + " (" + typePassager + ")";

            long count = resList.size();
            double total = finalPrice * count;

            details.add(new RevenueDetail(displayClass, finalPrice, count, total));
        }

        return details;
    }

}
