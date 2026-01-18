package com.fly.andco.repository.prix;

import com.fly.andco.model.prix.TarifVol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifVolRepository extends JpaRepository<TarifVol, Long> {
    List<TarifVol> findByVolInstance_IdVolInstance(Long idVolInstance);
    
    Optional<TarifVol> findByVolInstance_IdVolInstanceAndClasseAndTypePassager(
            Long idVolInstance, String classe, String typePassager);
}
