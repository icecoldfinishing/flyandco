package com.biblio.bibliotheque.service.pret;

import com.biblio.bibliotheque.model.pret.RegleJourApresRendu;
import com.biblio.bibliotheque.repository.pret.RegleJourApresRenduRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegleJourApresRenduService {

    @Autowired
    private RegleJourApresRenduRepository regleRepository;

    public List<RegleJourApresRendu> getAllRegles() {
        return regleRepository.findAll();
    }

    public Optional<RegleJourApresRendu> getById(Integer id) {
        return regleRepository.findById(id);
    }

    public RegleJourApresRendu save(RegleJourApresRendu regle) {
        return regleRepository.save(regle);
    }

    public void delete(Integer id) {
        regleRepository.deleteById(id);
    }

    public Integer getDelaiTolere() {
        List<RegleJourApresRendu> list = regleRepository.findAll();
        if (!list.isEmpty()) {
            return list.get(0).getNombreJour(); // On suppose qu’il y a une seule ligne
        }
        return 0; // Valeur par défaut si aucune règle n’est trouvée
    }
}
