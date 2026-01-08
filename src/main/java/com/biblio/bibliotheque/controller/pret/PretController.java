package com.biblio.bibliotheque.controller.pret;

import com.biblio.bibliotheque.model.gestion.*;
import com.biblio.bibliotheque.model.pret.*;
import com.biblio.bibliotheque.model.livre.Exemplaire;
import com.biblio.bibliotheque.model.gestion.Adherent;
import com.biblio.bibliotheque.model.gestion.Regle;
import com.biblio.bibliotheque.model.gestion.Utilisateur;
import com.biblio.bibliotheque.model.livre.Type;
import com.biblio.bibliotheque.repository.pret.*;

import jakarta.servlet.http.HttpSession;

import com.biblio.bibliotheque.repository.gestion.*;
import com.biblio.bibliotheque.repository.livre.*;
import com.biblio.bibliotheque.repository.pret.*;
import com.biblio.bibliotheque.service.gestion.AdherentService;
import com.biblio.bibliotheque.service.livre.*;
import com.biblio.bibliotheque.service.pret.*;
import com.biblio.bibliotheque.service.gestion.*;
import com.biblio.bibliotheque.repository.pret.PretRepository;
import com.biblio.bibliotheque.repository.livre.ExemplaireRepository;
import com.biblio.bibliotheque.repository.livre.TypeRepository;
import com.biblio.bibliotheque.service.sanction.SanctionService;

import java.util.Optional;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/preter")
public class PretController {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private PretService pretService;

    @Autowired
    private SanctionService sanctionService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private ProfilService profilService;

    @Autowired
    private RegleService regleService;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private DatePrevueRenduService datePrevueRenduService;

    @Autowired
    private TypeService typeService;
    
    @Autowired
    private RenduService renduService;

    @Autowired
    private RegleJourApresRenduService regleJourApresRenduService;

    @Autowired
    private PretDemandeService pretDemandeService;


    

    @GetMapping("/formpreter/livre")
    public String showFormPreterLivre(Model model) {
        model.addAttribute("pret", new Pret());
        model.addAttribute("exemplaires", exemplaireRepository.findAll());
        model.addAttribute("adherents", adherentService.getAll());
        model.addAttribute("types", typeRepository.findAll());
        return "views/preter/form_preter";
    }

    @PostMapping("/add")
    public String savePretDemande(@ModelAttribute Pret pret, Model model) {
        // Fixer un type par défaut si null
        if (pret.getType() == null) {
            Integer idTypeParDefaut = 1; // ID type par défaut dans ta base
            Type typeParDefaut = typeService.getTypeById(idTypeParDefaut)
                .orElseThrow(() -> new RuntimeException("Type par défaut introuvable"));
            pret.setType(typeParDefaut);
        }

        Integer idAdherent = pret.getAdherent().getIdAdherent();
        LocalDate dateDebut = pret.getDate_debut();
        Integer idExemplaire = pret.getExemplaire().getId_exemplaire();

        Integer idLivre = livreService.getIdLivreByIdExemplaire(idExemplaire);
        Integer ageRestriction = (idLivre != null) ? livreService.getAgeRestrictionByIdLivre(idLivre) : null;
        Integer ageAdherent = adherentService.getAgeAtDate(idAdherent, dateDebut);

        List<DatePrevueRendu> datesPrevues = datePrevueRenduService.getAllDatesPrevuesByAdherentIdAndDate(idAdherent, dateDebut);

        int delaiTolere = regleJourApresRenduService.getDelaiTolere();

        for (DatePrevueRendu dpr : datesPrevues) {
            if (dpr.getPret() != null && dpr.getPret().getId_pret() != null) {
                Integer idPret = dpr.getPret().getId_pret();
                Optional<Rendu> optRendu = renduService.getRenduByPretId(idPret);

                if (optRendu.isEmpty()) {
                    model.addAttribute("message", "❌ Le prêt #" + idPret + " n’a pas encore été rendu. Impossible de faire une nouvelle demande.");
                    return "views/preter/verification_pret";
                } else {
                    Rendu rendu = optRendu.get();
                    LocalDate dateRendu = rendu.getDateDuRendu();
                    LocalDate datePrevue = dpr.getDatePrevue();

                    if (dateRendu.isAfter(datePrevue)) {
                        LocalDate dateAutorisee = dateRendu.plusDays(delaiTolere);
                        if (dateAutorisee.isAfter(dateDebut)) {
                            model.addAttribute("message", "❌ Le prêt #" + idPret + " a été rendu en retard. Nouvelle demande possible seulement après le : " + dateAutorisee);
                            return "views/preter/verification_pret";
                        }
                    }
                }
            }
        }

        Optional<Adherent> optionalAdherent = adherentService.getById(idAdherent);
        if (optionalAdherent.isEmpty()) {
            model.addAttribute("message", "Adhérent introuvable.");
            return "views/preter/verification_pret";
        }

        Adherent adherent = optionalAdherent.get();
        Integer idProfil = adherent.getProfil().getId_profil();
        Integer idRegle = profilService.getIdRegleByIdProfil(idProfil);

        Regle regle = (idRegle != null) ? regleService.getById(idRegle).orElse(null) : null;

        String statut = adherentService.getStatutAdherentOnDate(idAdherent, dateDebut);
        boolean disponible = exemplaireService.isExemplaireDisponible(idExemplaire);
        boolean isSanctioned = sanctionService.isAdherentSanctioned(idAdherent, dateDebut.atStartOfDay());

        int nbMaxPrets = (regle != null) ? regle.getNb_livre_preter_max() : 0;
        int nbPretsActifs = pretService.countPretsActifsParAdherentALaDate(idAdherent, dateDebut);

        if (regle != null && dateDebut != null) {
            LocalDate dateFin = pretService.ajusterDateFin(dateDebut, regle.getNb_jour_duree_pret_max());
            pret.setDate_fin(dateFin);
            model.addAttribute("dateFin", dateFin);
        }

        model.addAttribute("datesPrevues", datesPrevues);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("idAdherent", idAdherent);
        model.addAttribute("idExemplaire", idExemplaire);
        model.addAttribute("idLivre", idLivre);
        model.addAttribute("ageRestriction", ageRestriction);
        model.addAttribute("ageAdherent", ageAdherent);
        model.addAttribute("statut", statut);
        model.addAttribute("disponible", disponible);
        model.addAttribute("isSanctioned", isSanctioned);
        model.addAttribute("idRegle", idRegle);
        model.addAttribute("regle", regle);

        if (!isSanctioned &&
            nbPretsActifs < nbMaxPrets &&
            disponible &&
            (ageRestriction == null || ageAdherent >= ageRestriction) &&
            "actif".equalsIgnoreCase(statut)) {

            // Convertir Pret en PretDemande (ou reconstruire un PretDemande)
            PretDemande demande = new PretDemande();
            demande.setAdherent(pret.getAdherent());
            demande.setExemplaire(pret.getExemplaire());
            demande.setType(pret.getType());
            demande.setDate_debut(pret.getDate_debut());
            demande.setDate_fin(pret.getDate_fin());

            PretDemande demandeSauvegarde = pretDemandeService.savePretDemande(demande);

            model.addAttribute("message", "✅ La demande de prêt a été enregistrée avec succès !");
        } else if (isSanctioned) {
            model.addAttribute("message", "❌ L'adhérent est sanctionné à cette date.");
        } else if (nbPretsActifs >= nbMaxPrets) {
            model.addAttribute("message", "❌ L'adhérent a déjà atteint la limite de prêts (" + nbMaxPrets + ").");
        } else if (!disponible) {
            model.addAttribute("message", "❌ L'exemplaire n'est pas disponible.");
        } else if (ageRestriction != null && ageAdherent < ageRestriction) {
            model.addAttribute("message", "❌ L'adhérent n'a pas l'âge requis (" + ageRestriction + " ans) pour emprunter ce livre.");
        } else if (!"actif".equalsIgnoreCase(statut)) {
            model.addAttribute("message", "❌ L'adhérent est inactif à cette date.");
        }

        return "views/preter/verification_pret";
    }



    // DELETE ACTION
    @GetMapping("/delete/{id}")
    public String deletePret(@PathVariable("id") Integer id) {
        pretRepository.deleteById(id);
        return "redirect:/preter/liste";
    }

    @GetMapping("/liste")
    public String listPretsByAdherent(Model model, HttpSession session) {
        // Récupérer l'utilisateur depuis la session
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("user");

        // Si pas d'utilisateur en session, rediriger vers login (optionnel)
        if (utilisateur == null) {
            return "redirect:/login"; 
        }

        // Trouver l'adhérent lié à cet utilisateur
        Adherent adherent = adherentRepository.findByUtilisateur(utilisateur)
                .orElse(null); 

        if (adherent == null) {
            model.addAttribute("error", "Aucun adhérent trouvé");
            return "pret/list"; 
        }

        // Récupérer les prêts de cet adhérent
        List<Pret> prets = pretRepository.findPretsNonRendusByAdherent(adherent);

        model.addAttribute("prets", prets);
        return "pret/list";
    }

    // Dans PretController.java
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleNotFound(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // Créez une page error.html pour afficher les messages d'erreur
    }

}
