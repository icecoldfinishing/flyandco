package com.biblio.bibliotheque.repository.livre;

import com.biblio.bibliotheque.model.livre.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Integer> {

    @Query("SELECT e.livre.idLivre FROM Exemplaire e WHERE e.id_exemplaire = :idExemplaire")
    Integer getIdLivreByIdExemplaire(@Param("idExemplaire") Integer idExemplaire);

    @Query(value = "SELECT e.id_exemplaire FROM exemplaire e LEFT JOIN pret p ON e.id_exemplaire = p.id_exemplaire WHERE e.id_livre = :idLivre AND (p.id_pret IS NULL OR p.date_fin < NOW()) LIMIT 1", nativeQuery = true)
    Integer findAvailableExemplaire(@Param("idLivre") Integer idLivre);

    List<Exemplaire> findByLivreIdLivre(Integer idLivre);


}
