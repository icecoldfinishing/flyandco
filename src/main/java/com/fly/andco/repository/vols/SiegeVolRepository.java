package com.fly.andco.repository.vols;

import com.fly.andco.model.vols.SiegeVol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiegeVolRepository extends JpaRepository<SiegeVol, Long> {
    List<SiegeVol> findByVolInstance_IdVolInstance(Long idVolInstance);
    // Find free seats for a generic booking query
    List<SiegeVol> findByVolInstance_IdVolInstanceAndStatut(Long idVolInstance, String statut);
}
