package com.biblio.bibliotheque.service.pret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.biblio.bibliotheque.model.pret.PretDemande;
import com.biblio.bibliotheque.repository.pret.PretDemandeRepository;
import java.util.List;

@Service
public class PretDemandeService {

    @Autowired
    private PretDemandeRepository pretDemandeRepository;

    public PretDemande savePretDemande(PretDemande pretDemande) {
        return pretDemandeRepository.save(pretDemande);
    }
    public List<PretDemande> getAllPretDemandes() {
        return pretDemandeRepository.findAll();
    }

    public PretDemande getById(Integer id) {
        return pretDemandeRepository.findById(id).orElse(null);
    }

    public void deleteById(Integer id) {
        pretDemandeRepository.deleteById(id);
    }
}
