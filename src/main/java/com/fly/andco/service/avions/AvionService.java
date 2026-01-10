package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.repository.avions.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvionService {

    private final AvionRepository avionRepository;

    @Autowired
    public AvionService(AvionRepository avionRepository) {
        this.avionRepository = avionRepository;
    }

    // Lister tous les avions
    public List<Avion> getAllAvions() {
        return avionRepository.findAll();
    }

    // Enregistrer un nouvel avion ou mettre à jour un existant
    public Avion saveAvion(Avion avion) {
        return avionRepository.save(avion);
    }

    // Récupérer un avion par son ID
    public Optional<Avion> getAvionById(Long id) {
        return avionRepository.findById(id);
    }

    // Supprimer un avion par son ID
    public void deleteAvion(Long id) {
        avionRepository.deleteById(id);
    }

    // Vérifier si un avion existe par son ID
    public boolean existsById(Long id) {
        return avionRepository.existsById(id);
    }
}
