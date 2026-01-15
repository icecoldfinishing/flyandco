package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Siege;
import com.fly.andco.repository.avions.SiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiegeService {

    private final SiegeRepository siegeRepository;

    @Autowired
    public SiegeService(SiegeRepository siegeRepository) {
        this.siegeRepository = siegeRepository;
    }

    // Lister tous les sièges
    public List<Siege> getAllSieges() {
        return siegeRepository.findAll();
    }

    // Lister les sièges d’un avion
    public List<Siege> getSiegesByAvion(Long avionId) {
        return siegeRepository.findByAvion_IdAvion(avionId);
    }

    // Enregistrer ou mettre à jour un siège
    public Siege saveSiege(Siege siege) {
        return siegeRepository.save(siege);
    }

    // Récupérer un siège par ID
    public Optional<Siege> getSiegeById(Long id) {
        return siegeRepository.findById(id);
    }

    // Supprimer un siège
    public void deleteSiege(Long id) {
        siegeRepository.deleteById(id);
    }

    // Vérifier existence
    public boolean existsById(Long id) {
        return siegeRepository.existsById(id);
    }

    @Autowired
    private com.fly.andco.repository.prix.PrixVolRepository prixVolRepository;

    public Double calculateMaxRevenue(Long avionId, Long volId) {
        List<Siege> sieges = siegeRepository.findByAvion_IdAvion(avionId);
        List<com.fly.andco.model.prix.PrixVol> prixVols = prixVolRepository.findAll(); // Optimization: should filter by volId

        // Filter prices for the specific Vol
        List<com.fly.andco.model.prix.PrixVol> prixForVol = prixVols.stream()
                .filter(p -> p.getVol().getIdVol().equals(volId))
                .toList();

        double totalRevenue = 0.0;

        for (Siege siege : sieges) {
            String classe = siege.getClasse();
            // Find price for this class
            Optional<com.fly.andco.model.prix.PrixVol> prixOpt = prixForVol.stream()
                    .filter(p -> p.getClasse().equalsIgnoreCase(classe))
                    .findFirst();

            if (prixOpt.isPresent()) {
                totalRevenue += prixOpt.get().getPrix();
            }
        }

        return totalRevenue;
    }

}
