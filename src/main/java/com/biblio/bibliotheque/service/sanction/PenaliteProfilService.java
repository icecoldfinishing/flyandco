package com.biblio.bibliotheque.service.sanction;

import com.biblio.bibliotheque.model.sanction.PenaliteProfil;
import com.biblio.bibliotheque.repository.sanction.PenaliteProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaliteProfilService {

    @Autowired
    private PenaliteProfilRepository penaliteProfilRepository;

    public List<PenaliteProfil> getPenalitesByProfil(Integer idProfil) {
        return penaliteProfilRepository.findByProfilId_profil(idProfil);
    }
}