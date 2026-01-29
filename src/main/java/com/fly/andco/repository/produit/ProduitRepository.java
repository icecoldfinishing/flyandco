package com.fly.andco.repository.produit;

import com.fly.andco.model.produit.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
	List<Produit> findByVolInstance_IdVolInstance(Long idVolInstance);
	List<Produit> findBySociete_IdSociete(Integer idSociete);
	List<Produit> findByNomIgnoreCaseAndSociete_IdSocieteAndVolInstance_IdVolInstance(String nom, Integer idSociete, Long idVolInstance);
}

