
package com.biblio.bibliotheque.service.reservation;

import com.biblio.bibliotheque.model.gestion.Adherent;
import com.biblio.bibliotheque.model.gestion.Regle;
import com.biblio.bibliotheque.model.gestion.Statut;
import com.biblio.bibliotheque.model.livre.Type;
import com.biblio.bibliotheque.model.pret.Pret;
import com.biblio.bibliotheque.model.reservation.Reservation;
import com.biblio.bibliotheque.model.reservation.StatutReservation;
import com.biblio.bibliotheque.repository.gestion.AdherentRepository;
import com.biblio.bibliotheque.repository.gestion.RegleRepository;
import com.biblio.bibliotheque.repository.gestion.StatutRepository;
import com.biblio.bibliotheque.repository.livre.ExemplaireRepository;
import com.biblio.bibliotheque.repository.livre.TypeRepository;
import com.biblio.bibliotheque.repository.pret.PretRepository;
import com.biblio.bibliotheque.repository.pret.ProlongementRepository;
import com.biblio.bibliotheque.repository.reservation.ReservationRepository;
import com.biblio.bibliotheque.repository.reservation.StatutReservationRepository;
import com.biblio.bibliotheque.repository.sanction.SanctionRepository;
import com.biblio.bibliotheque.service.sanction.SanctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StatutReservationRepository statutReservationRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private SanctionService sanctionService;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private StatutRepository statutRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ProlongementRepository prolongementRepository;

    @Autowired
    private RegleRepository regleRepository;

    public void creerReservation(Integer idLivre, Integer idAdherent, Date dateReservation) throws Exception {
        // Find an available exemplaire for the given livre
        Integer idExemplaire = exemplaireRepository.findAvailableExemplaire(idLivre);

        if (idExemplaire == null) {
            throw new Exception("Aucun exemplaire disponible pour ce livre.");
        }

        // Check if the adherent is sanctioned
        if (sanctionService.isAdherentSanctioned(idAdherent, dateReservation.toLocalDate().atStartOfDay())) {
            throw new Exception("L'adhérent est sanctionné et ne peut pas réserver.");
        }

        // Check if the book is already loaned out for the requested date
        if (pretRepository.isLivrePrete(idLivre, dateReservation)) {
            throw new Exception("Le livre est déjà en prêt pour la date demandée.");
        }

        // Check if the previous holder prolonged their loan
        Optional<Pret> lastPret = pretRepository.findLastPretByExemplaireId(idExemplaire);
        if (lastPret.isPresent() && prolongementRepository.hasProlonged(lastPret.get().getId_pret())) {
            throw new Exception("Le précédent détenteur a prolongé son prêt pour ce livre.");
        }

        // Get adherent's rules
        Adherent adherent = adherentRepository.findById(idAdherent)
                .orElseThrow(() -> new Exception("Adhérent non trouvé."));
        Regle regle = adherent.getProfil().getRegle();
        int nbLivrePreterMax = regle.getNb_livre_preter_max();

        // Count active loans for the adherent within the reservation period
        int activeLoans = pretRepository.countActiveLoansForAdherent(idAdherent, dateReservation.toLocalDate(), dateReservation.toLocalDate().plusDays(7));

        // Count pending reservations for the adherent
        int pendingReservations = reservationRepository.countPendingReservationsForAdherentAndLivre(idAdherent, idLivre);

        // Check if the adherent has reached the maximum loan/reservation limit
        if (activeLoans + pendingReservations >= nbLivrePreterMax) {
            throw new Exception("Vous avez atteint le nombre maximum de livres que vous pouvez emprunter ou réserver (" + nbLivrePreterMax + ").");
        }

        // Check if there's already a pending reservation for the same book by the same adherent
        if (pendingReservations > 0) {
            throw new Exception("Vous avez déjà une réservation en attente pour ce livre.");
        }

        Reservation reservation = new Reservation();
        reservation.setExemplaire(exemplaireRepository.findById(idExemplaire).get());
        reservation.setAdherent(adherent);
        reservation.setDate_reservation(LocalDate.now());
        reservation.setDate_debut_reservation(dateReservation.toLocalDate());
        reservation.setDate_fin_reservation(dateReservation.toLocalDate().plusDays(7)); // 7 days reservation
        reservationRepository.save(reservation);

        StatutReservation statutReservation = new StatutReservation();
        statutReservation.setReservation(reservation);
        Statut enAttente = statutRepository.findById(1).get(); // 1 = en attente
        statutReservation.setStatut(enAttente);
        statutReservation.setDate_modif(LocalDateTime.now());
        statutReservationRepository.save(statutReservation);
    }

    public void validerReservation(Integer idReservation) throws Exception {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new Exception("Réservation non trouvée"));

        // Create a new Pret
        Pret pret = new Pret();
        pret.setExemplaire(reservation.getExemplaire());
        pret.setAdherent(reservation.getAdherent());
        pret.setDate_debut(reservation.getDate_debut_reservation());
        pret.setDate_fin(reservation.getDate_debut_reservation().plusDays(14)); // 14 days loan
        Type maison = typeRepository.findById(1).get(); // 1 = Maison
        pret.setType(maison);
        pretRepository.save(pret);

        // Update the reservation status
        StatutReservation statutReservation = new StatutReservation();
        statutReservation.setReservation(reservation);
        Statut valide = statutRepository.findById(2).get(); // 2 = valider
        statutReservation.setStatut(valide);
        statutReservation.setDate_modif(LocalDateTime.now());
        statutReservationRepository.save(statutReservation);
    }

    public void rejectReservation(Integer idReservation) throws Exception {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new Exception("Réservation non trouvée"));

        StatutReservation statutReservation = new StatutReservation();
        statutReservation.setReservation(reservation);
        Statut valide = statutRepository.findById(2).get(); // 2 = valider (as per instruction)
        statutReservation.setStatut(valide);
        statutReservation.setDate_modif(LocalDateTime.now());
        statutReservationRepository.save(statutReservation);
    }
}
