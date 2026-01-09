package com.fly.andco.service.prix;

import com.fly.andco.model.prix.PrixVol;
import com.fly.andco.repository.prix.PrixVolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrixVolService {

    @Autowired
    private PrixVolRepository prixVolRepository;

    public List<PrixVol> getAll() {
        return prixVolRepository.findAll();
    }

    public Optional<PrixVol> getById(Long id) {
        return prixVolRepository.findById(id);
    }

    public PrixVol save(PrixVol prixVol) {
        return prixVolRepository.save(prixVol);
    }

    public void delete(Long id) {
        prixVolRepository.deleteById(id);
    }
}
