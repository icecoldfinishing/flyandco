package com.biblio.bibliotheque.repository.pret;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblio.bibliotheque.model.pret.DatePrevueRendu;

public interface DatePrevueRenduRepository extends JpaRepository<DatePrevueRendu, Integer> {

    // Requête explicite pour éviter problème de nom de propriété id_pret (clé primaire dans Pret)
    @Query("SELECT d FROM DatePrevueRendu d WHERE d.pret.id_pret = :idPret")
    Optional<DatePrevueRendu> findByPretId(@Param("idPret") Integer idPret);

    // Récupérer toutes les dates prévues pour un adhérent donné (idAdherent)
    @Query("SELECT d FROM DatePrevueRendu d WHERE d.pret.adherent.idAdherent = :idAdherent AND d.datePrevue <= :dateMax")
    List<DatePrevueRendu> findAllByAdherentIdAndDatePrevueBeforeOrEqual(
        @Param("idAdherent") Integer idAdherent,
        @Param("dateMax") LocalDate dateMax);

}
