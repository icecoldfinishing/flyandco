package com.fly.andco.repository.paiements;

import com.fly.andco.model.paiements.MoyenPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoyenPaiementRepository extends JpaRepository<MoyenPaiement, Long> {
}
