package com.fly.andco.service.produit;

import com.fly.andco.model.produit.VenteProduit;
import com.fly.andco.repository.produit.VenteProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenteProduitService {

    private final VenteProduitRepository venteProduitRepository;

    public VenteProduitService(VenteProduitRepository venteProduitRepository) {
        this.venteProduitRepository = venteProduitRepository;
    }

    public List<VenteProduit> getAll() {
        return venteProduitRepository.findAll();
    }

    public VenteProduit save(VenteProduit vente) {
        return venteProduitRepository.save(vente);
    }
}
