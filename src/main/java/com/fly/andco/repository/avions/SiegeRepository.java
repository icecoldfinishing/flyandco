package com.fly.andco.repository.avions;

import com.fly.andco.model.avions.Siege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiegeRepository extends JpaRepository<Siege, Long> {

    // Correct: utiliser le nom du champ de l'objet Vol + son id
    List<Siege> findByAvion_IdAvion(Long idAvion);

}
