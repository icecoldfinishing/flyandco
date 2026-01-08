package com.biblio.bibliotheque.service.sanction;

import com.biblio.bibliotheque.model.gestion.Adherent;
import com.biblio.bibliotheque.model.sanction.Sanction;
import com.biblio.bibliotheque.repository.gestion.AdherentRepository;
import com.biblio.bibliotheque.repository.sanction.SanctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SanctionService {

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    public List<Sanction> getSanctionsByAdherent(Integer idAdherent) {
        return sanctionRepository.findByAdherentIdAdherent(idAdherent);
    }

    public boolean isAdherentSanctioned(Integer idAdherent, LocalDateTime date) {
        List<Sanction> sanctions = sanctionRepository.findByAdherentIdAdherentAndDate(idAdherent, date);
        return !sanctions.isEmpty();
    }

    public void sanctionnerAdherent(Integer idAdherent, LocalDate dateDebut, LocalDate dateFin, String motif) {
        Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow(() -> new RuntimeException("Adherent not found"));
        Sanction sanction = new Sanction();
        sanction.setAdherent(adherent);
        sanction.setDate_debut(dateDebut.atStartOfDay());
        sanction.setDate_fin(dateFin.atStartOfDay());
        sanction.setMotif(motif);
        sanction.setDate_sanction(LocalDateTime.now());
        sanctionRepository.save(sanction);
    }
}