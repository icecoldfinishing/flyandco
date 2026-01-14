package com.fly.andco.repository.compagnies;

import com.fly.andco.model.compagnies.ViewChiffreAffaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewChiffreAffaireRepository extends JpaRepository<ViewChiffreAffaire, Long> {

    List<ViewChiffreAffaire> findByIdCompagnie(Long idCompagnie);
    List<ViewChiffreAffaire> findByIdAvion(Long idAvion);
    List<ViewChiffreAffaire> findByIdVol(Long idVol);
    List<ViewChiffreAffaire> findByIdVolInstance(Long idVolInstance);
    List<ViewChiffreAffaire> findByDatePaiementBetween(LocalDateTime start, LocalDateTime end);
}
