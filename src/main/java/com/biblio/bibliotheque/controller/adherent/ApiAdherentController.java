package com.biblio.bibliotheque.controller.adherent;

import com.biblio.bibliotheque.model.gestion.Adherent;
import com.biblio.bibliotheque.model.sanction.PenaliteProfil;
import com.biblio.bibliotheque.model.sanction.Sanction;
import com.biblio.bibliotheque.service.gestion.AdherentService;
import com.biblio.bibliotheque.model.pret.Pret;
import com.biblio.bibliotheque.service.pret.PretService;
import com.biblio.bibliotheque.service.sanction.PenaliteProfilService;
import com.biblio.bibliotheque.service.sanction.SanctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adherent")
public class ApiAdherentController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private SanctionService sanctionService;

    @Autowired
    private PenaliteProfilService penaliteProfilService;

    @Autowired
    private PretService pretService;

    @GetMapping("")
    public Map<String, Object> getAllAdherents() {
        Map<String, Object> response = new HashMap<>();
        List<Adherent> adherents = adherentService.getAll();
        List<Map<String, Object>> adherentsList = adherents.stream()
                .map(this::buildAdherentMap)
                .collect(Collectors.toList());
        response.put("adherents", adherentsList);
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getAdherentById(@PathVariable Integer id) {
        Optional<Adherent> adherentOpt = adherentService.getById(id);
        if (adherentOpt.isPresent()) {
            return buildAdherentMap(adherentOpt.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Adherent non trouvé avec l'ID: " + id);
            return response;
        }
    }

    private Map<String, Object> buildAdherentMap(Adherent adherent) {
        Map<String, Object> adherentMap = new HashMap<>();
        adherentMap.put("idAdherent", adherent.getIdAdherent());
        adherentMap.put("nom", adherent.getNom());
        adherentMap.put("prenom", adherent.getPrenom());
        adherentMap.put("dateDeNaissance", adherent.getDateDeNaissance());
        adherentMap.put("statut", adherentService.getStatutAdherentOnDate(adherent.getIdAdherent(), LocalDate.now()));

        if (adherent.getProfil() != null) {
            Integer quota = adherent.getProfil().getRegle().getNb_livre_preter_max();
            Integer unreturnedBooks = pretService.getPretNonRendusByAdherent(adherent).size();
            Integer quotaRestant = quota - unreturnedBooks;

            adherentMap.put("profil", adherent.getProfil().getNom());
            adherentMap.put("quota", quota);
            adherentMap.put("quotaRestant", quotaRestant);

            List<PenaliteProfil> penalites = penaliteProfilService.getPenalitesByProfil(adherent.getProfil().getId_profil());
            List<Map<String, Object>> penalitesMap = penalites.stream().map(penalite -> {
                Map<String, Object> penaliteMap = new HashMap<>();
                penaliteMap.put("nb_jour_de_penalite", penalite.getPenalite().getNb_jour_de_penalite());
                penaliteMap.put("date_modif", penalite.getDate_modif());
                return penaliteMap;
            }).collect(Collectors.toList());
            adherentMap.put("penalites", penalitesMap);
        }

        List<Sanction> sanctions = sanctionService.getSanctionsByAdherent(adherent.getIdAdherent());
        List<Map<String, Object>> sanctionsMap = sanctions.stream().map(sanction -> {
            Map<String, Object> sanctionMap = new HashMap<>();
            sanctionMap.put("date_debut", sanction.getDate_debut());
            sanctionMap.put("date_fin", sanction.getDate_fin());
            sanctionMap.put("motif", sanction.getMotif());
            return sanctionMap;
        }).collect(Collectors.toList());
        adherentMap.put("sanctions", sanctionsMap);

        List<Pret> pretsNonRendus = pretService.getPretNonRendusByAdherent(adherent);
        List<Map<String, Object>> pretsNonRendusMap = pretsNonRendus.stream().map(pret -> {
            Map<String, Object> pretMap = new HashMap<>();
            pretMap.put("id_pret", pret.getId_pret());
            pretMap.put("date_debut", pret.getDate_debut());
            pretMap.put("date_fin", pret.getDate_fin());
            pretMap.put("titre_livre", pret.getExemplaire().getLivre().getTitre());
            return pretMap;
        }).collect(Collectors.toList());
        adherentMap.put("prets_non_rendus", pretsNonRendusMap);

        return adherentMap;
    }
}
