package com.fly.andco.service.compagnies;

import com.fly.andco.model.compagnies.ViewChiffreAffaire;
import com.fly.andco.repository.compagnies.ViewChiffreAffaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewChiffreAffaireService {

    @Autowired
    private ViewChiffreAffaireRepository viewCARepo;

    public List<ViewChiffreAffaire> getAll() {
        return viewCARepo.findAll();
    }

    public List<ViewChiffreAffaire> getByCompagnie(Long idCompagnie) {
        return viewCARepo.findByIdCompagnie(idCompagnie);
    }

    public List<ViewChiffreAffaire> getByAvion(Long idAvion) {
        return viewCARepo.findByIdAvion(idAvion);
    }

    public List<ViewChiffreAffaire> getByVol(Long idVol) {
        return viewCARepo.findByIdVol(idVol);
    }

    public List<ViewChiffreAffaire> getByVolInstance(Long idVolInstance) {
        return viewCARepo.findByIdVolInstance(idVolInstance);
    }
}
