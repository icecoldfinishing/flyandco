package com.biblio.bibliotheque.repository.pret;

import com.biblio.bibliotheque.model.pret.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProlongementRepository extends JpaRepository<Prolongement, Integer> {
    // Méthodes personnalisées si besoin
    @Query("SELECT COUNT(p) > 0 FROM Prolongement p WHERE p.pret.id_pret = :idPret")
    boolean hasProlonged(@Param("idPret") Integer idPret);
}
