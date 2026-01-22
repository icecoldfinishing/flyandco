package com.fly.andco.repository;

import com.fly.andco.model.TarifPublicitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fly.andco.model.Compagnie;
import java.util.Optional;

public interface TarifPublicitaireRepository extends JpaRepository<TarifPublicitaire, Integer> {
    Optional<TarifPublicitaire> findByCompagnie(Compagnie compagnie);
}
