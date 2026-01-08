package com.biblio.bibliotheque.repository.sanction;

import com.biblio.bibliotheque.model.sanction.PenaliteProfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaliteProfilRepository extends JpaRepository<PenaliteProfil, Integer> {
    @Query("SELECT pp FROM PenaliteProfil pp WHERE pp.profil.id_profil = :idProfil")
    List<PenaliteProfil> findByProfilId_profil(@Param("idProfil") Integer idProfil);
}