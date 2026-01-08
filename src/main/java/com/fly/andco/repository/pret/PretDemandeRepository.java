package com.biblio.bibliotheque.repository.pret;

import com.biblio.bibliotheque.model.pret.PretDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretDemandeRepository extends JpaRepository<PretDemande, Integer> {
    // méthodes personnalisées éventuelles
}
