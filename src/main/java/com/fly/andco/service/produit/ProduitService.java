package com.fly.andco.service.produit;

import com.fly.andco.model.produit.Produit;
import com.fly.andco.model.produit.VenteProduit;
import com.fly.andco.repository.produit.ProduitRepository;
import com.fly.andco.repository.produit.VenteProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private VenteProduitRepository venteProduitRepository;

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}

	public Optional<Produit> getById(Long id) {
		return produitRepository.findById(id);
	}

	// Produits liés à vol instance now come from sales entries
	public List<VenteProduit> getVentesByVolInstance(Long idVolInstance) {
		return venteProduitRepository.findByVolInstance_IdVolInstance(idVolInstance);
	}

	public List<Produit> getBySociete(Integer idSociete) {
		return produitRepository.findBySociete_IdSociete(idSociete);
	}

	public Produit save(Produit produit) {
		return produitRepository.save(produit);
	}

	public void delete(Long id) {
		produitRepository.deleteById(id);
	}

	// Stock management should be handled per sale; keeping placeholder if needed

	public java.math.BigDecimal getRevenueForVolInstance(Long idVolInstance) {
		java.util.List<VenteProduit> ventes = venteProduitRepository.findByVolInstance_IdVolInstance(idVolInstance);
		java.math.BigDecimal total = java.math.BigDecimal.ZERO;
		for (VenteProduit v : ventes) {
			if (v.getProduit() != null && v.getProduit().getPrix() != null && v.getQuantite() != null) {
				total = total.add(v.getProduit().getPrix().multiply(java.math.BigDecimal.valueOf(v.getQuantite())));
			}
		}
		return total;
	}
}

