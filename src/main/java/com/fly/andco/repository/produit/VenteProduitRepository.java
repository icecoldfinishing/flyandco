package com.fly.andco.repository.produit;

import com.fly.andco.model.produit.VenteProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Long> {
    List<VenteProduit> findByVolInstance_IdVolInstance(Long idVolInstance);
}
