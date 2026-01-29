package com.fly.andco.service.produit;

import com.fly.andco.model.produit.Produit;
import com.fly.andco.repository.produit.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

	@Autowired
	private ProduitRepository produitRepository;

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}

	public Optional<Produit> getById(Long id) {
		return produitRepository.findById(id);
	}

	public List<Produit> getByVolInstance(Long idVolInstance) {
		return produitRepository.findByVolInstance_IdVolInstance(idVolInstance);
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

	public Optional<Produit> updateStock(Long idProduit, int delta) {
		Optional<Produit> opt = produitRepository.findById(idProduit);
		opt.ifPresent(p -> {
			int nouveau = (p.getNombre() == null ? 0 : p.getNombre()) + delta;
			if (nouveau < 0) {
				nouveau = 0;
			}
			p.setNombre(nouveau);
			produitRepository.save(p);
		});
		return opt;
	}

	public java.math.BigDecimal getRevenueForVolInstance(Long idVolInstance) {
		java.util.List<Produit> produits = produitRepository.findByVolInstance_IdVolInstance(idVolInstance);
		java.math.BigDecimal total = java.math.BigDecimal.ZERO;
		for (Produit p : produits) {
			if (p.getPrix() != null && p.getNombre() != null) {
				total = total.add(p.getPrix().multiply(java.math.BigDecimal.valueOf(p.getNombre())));
			}
		}
		return total;
	}
}

