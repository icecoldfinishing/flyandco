package com.biblio.bibliotheque.repository.livre;

import com.biblio.bibliotheque.model.livre.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Integer> {
    
}
