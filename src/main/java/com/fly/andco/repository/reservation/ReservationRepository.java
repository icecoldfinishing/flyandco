package com.biblio.bibliotheque.repository.reservation;

import com.biblio.bibliotheque.model.livre.Exemplaire;
import com.biblio.bibliotheque.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    boolean existsByExemplaire(Exemplaire exemplaire);

    @Query("SELECT r FROM Reservation r")
    List<Reservation> findAllWithStatutReservation();

    @Query(value = "SELECT COUNT(r.*) FROM reservation r JOIN exemplaire e ON r.id_exemplaire = e.id_exemplaire JOIN statut_reservation sr ON r.id_reservation = sr.id_reservation WHERE r.id_adherent = :idAdherent AND e.id_livre = :idLivre AND sr.id_statut = 1", nativeQuery = true)
    Integer countPendingReservationsForAdherentAndLivre(@Param("idAdherent") Integer idAdherent, @Param("idLivre") Integer idLivre);
}
