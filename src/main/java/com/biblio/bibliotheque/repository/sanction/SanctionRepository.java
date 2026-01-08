package com.biblio.bibliotheque.repository.sanction;

import com.biblio.bibliotheque.model.sanction.Sanction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Integer> {
    List<Sanction> findByAdherentIdAdherent(Integer idAdherent);

    @Query("SELECT s FROM Sanction s WHERE s.adherent.idAdherent = :idAdherent AND :date BETWEEN s.date_debut AND s.date_fin")
    List<Sanction> findByAdherentIdAdherentAndDate(@Param("idAdherent") Integer idAdherent, @Param("date") LocalDateTime date);
}