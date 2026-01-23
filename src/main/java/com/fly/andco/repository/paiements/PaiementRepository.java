package com.fly.andco.repository.paiements;

import com.fly.andco.model.paiements.Paiement;
import com.fly.andco.model.vols.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.reservation.volInstance = :volInstance")
    BigDecimal sumMontantByVolInstance(@Param("volInstance") VolInstance volInstance);
    
    List<Paiement> findByReservation_VolInstance(VolInstance volInstance);
}
