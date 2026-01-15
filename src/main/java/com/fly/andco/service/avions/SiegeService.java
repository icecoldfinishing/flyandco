package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Siege;
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

    @Autowired
    public SiegeService(SiegeRepository siegeRepository, PrixVolRepository prixVolRepository) {
        this.siegeRepository = siegeRepository;
        this.prixVolRepository = prixVolRepository;
    }

    // Lister tous les sièges
    public List<Siege> getAllSieges() {
        return siegeRepository.findAll();
    }

    public java.util.List<com.fly.andco.dto.RevenueDetail> calculateMaxRevenue(Long volId) {
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

}
