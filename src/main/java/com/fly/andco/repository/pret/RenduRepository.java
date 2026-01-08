package com.biblio.bibliotheque.repository.pret;

import com.biblio.bibliotheque.model.pret.Rendu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RenduRepository extends JpaRepository<Rendu, Integer> {

    @Query("SELECT r FROM Rendu r WHERE r.pret.id_pret = :idPret")
    Optional<Rendu> findByPretId(@Param("idPret") Integer idPret);

}
