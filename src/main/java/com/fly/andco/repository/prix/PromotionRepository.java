package com.fly.andco.repository.prix;

import com.fly.andco.model.prix.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByPrixVol(com.fly.andco.model.prix.PrixVol prixVol);
}
