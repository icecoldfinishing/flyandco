package com.biblio.bibliotheque.controller.librarian;

import com.biblio.bibliotheque.model.livre.Etat;
import com.biblio.bibliotheque.model.livre.EtatExemplaire;
import com.biblio.bibliotheque.model.pret.*;
import com.biblio.bibliotheque.repository.livre.EtatExemplaireRepository;
import com.biblio.bibliotheque.repository.livre.EtatRepository;
import com.biblio.bibliotheque.repository.pret.*;
import com.biblio.bibliotheque.service.pret.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianPretController {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private PretService pretService;

    @Autowired
    private PretDemandeService pretDemandeService;

    @Autowired
    private DatePrevueRenduService datePrevueRenduService;

    @Autowired
    private EtatExemplaireRepository etatExemplaireRepository;

    @Autowired
    private EtatRepository etatRepository;

    // Afficher toutes les demandes de prêt
    @GetMapping("/pret_demande")
    public String afficherToutesLesDemandesDePret(Model model) {
        List<PretDemande> listeDemandes = pretDemandeService.getAllPretDemandes();
        model.addAttribute("listeDemandes", listeDemandes);
        return "librarian/pret/pret_demande";
    }

    @PostMapping("/accept/{id}")
    public String accepterDemande(@PathVariable("id") Integer id) {
        PretDemande demande = pretDemandeService.getById(id);

        if (demande != null) {
            // Étape 1 : Copier vers Pret
            Pret pret = new Pret();
            pret.setDate_debut(demande.getDate_debut());
            pret.setDate_fin(demande.getDate_fin());
            pret.setAdherent(demande.getAdherent());
            pret.setExemplaire(demande.getExemplaire());
            pret.setType(demande.getType());

            Pret pretSauvegarde = pretService.savePret(pret);

            // Étape 2 : Créer et enregistrer la date prévue de rendu
            DatePrevueRendu dpr = new DatePrevueRendu();
            dpr.setPret(pretSauvegarde);
            dpr.setDatePrevue(pretSauvegarde.getDate_fin());
            datePrevueRenduService.save(dpr);

            // Étape 3 : Mettre à jour l'état de l'exemplaire
            EtatExemplaire etatExemplaire = new EtatExemplaire();
            etatExemplaire.setExemplaire(demande.getExemplaire());
            Etat emprunte = etatRepository.findById(2).orElse(null); // 2 = Emprunté
            etatExemplaire.setEtat(emprunte);
            etatExemplaire.setDate_modif(java.time.LocalDateTime.now());
            etatExemplaireRepository.save(etatExemplaire);


            // Étape 4 : Supprimer la demande de prêt
            pretDemandeService.deleteById(id);
        }

        return "redirect:/librarian/pret_demande";
    }


    // Refuser une demande de prêt
    @PostMapping("/refuse/{id}")
    public String refuserDemande(@PathVariable("id") Integer id) {
        pretDemandeService.deleteById(id);
        return "redirect:/librarian/pret_demande";
    }
}
