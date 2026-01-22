package com.fly.andco.repository.publicite;

import com.fly.andco.model.publicite.TarifPublicitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fly.andco.model.compagnies.Compagnie;
import java.util.Optional;

public interface TarifPublicitaireRepository extends JpaRepository<TarifPublicitaire, Integer> {
    Optional<TarifPublicitaire> findByCompagnie(Compagnie compagnie);
}
