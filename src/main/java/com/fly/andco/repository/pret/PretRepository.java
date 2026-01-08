package com.biblio.bibliotheque.repository.pret;

import com.biblio.bibliotheque.model.gestion.Adherent;
import com.biblio.bibliotheque.model.livre.Exemplaire;
import com.biblio.bibliotheque.model.pret.Pret;

import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PretRepository extends JpaRepository<Pret, Integer> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
    List<Pret> findByAdherent(Adherent adherent);

    @Query("SELECT COUNT(p) FROM Pret p WHERE p.adherent.idAdherent = :idAdherent AND p.date_debut <= :nouvelleDate AND (p.date_fin IS NULL OR p.date_fin > :nouvelleDate)AND p.type.nom <> 'Sur place'")
    int countPretsActifsParAdherentALaDate(@Param("idAdherent") Integer idAdherent, @Param("nouvelleDate") LocalDate nouvelleDate);

    @Override
    boolean existsById(Integer id_pret);

    @Query("SELECT COUNT(p) > 0 FROM Pret p WHERE p.id_pret = :idPret AND p.adherent.idAdherent = :idAdherent")
    boolean pretAppartientAdherent(@Param("idPret") Integer idPret, @Param("idAdherent") Integer idAdherent);

    @Query("SELECT p.date_fin FROM Pret p WHERE p.id_pret = :idPret")
    LocalDate getDateFinById(@Param("idPret") Integer idPret);

    @Query("SELECT p FROM Pret p WHERE p.exemplaire = :exemplaire AND p.date_fin > :dateFin")
    Optional<Pret> findByExemplaireAndDateFinAfter(@Param("exemplaire") Exemplaire exemplaire, @Param("dateFin") LocalDate dateFin);

    @Query(value = "SELECT COUNT(*) > 0 FROM pret p JOIN exemplaire e ON p.id_exemplaire = e.id_exemplaire WHERE e.id_livre = :idLivre AND :dateReservation BETWEEN p.date_debut AND p.date_fin", nativeQuery = true)
    boolean isLivrePrete(@Param("idLivre") Integer idLivre, @Param("dateReservation") Date dateReservation);

    @Query(value = "SELECT * FROM pret WHERE id_exemplaire = :idExemplaire ORDER BY date_fin DESC LIMIT 1", nativeQuery = true)
    Optional<Pret> findLastPretByExemplaireId(@Param("idExemplaire") Integer idExemplaire);

    @Query(value = "SELECT COUNT(p.*) FROM pret p JOIN exemplaire e ON p.id_exemplaire = e.id_exemplaire WHERE p.id_adherent = :idAdherent AND p.date_debut <= :dateFin AND p.date_fin >= :dateDebut", nativeQuery = true)
    Integer countActiveLoansForAdherent(@Param("idAdherent") Integer idAdherent, @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("SELECT p FROM Pret p WHERE p.id_pret NOT IN (SELECT r.pret.id_pret FROM Rendu r)")
    List<Pret> findPretsNonRendus();

    @Query("SELECT p FROM Pret p WHERE p.adherent = :adherent AND p.id_pret NOT IN (SELECT r.pret.id_pret FROM Rendu r)")
    List<Pret> findPretsNonRendusByAdherent(@Param("adherent") Adherent adherent);
}
