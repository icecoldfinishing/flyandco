package com.fly.andco.service.equipages;

import com.fly.andco.model.equipages.Equipage;
import com.fly.andco.repository.equipages.EquipageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipageService {

    @Autowired
    private EquipageRepository equipageRepository;

    public List<Equipage> getAll() {
        return equipageRepository.findAll();
    }

    public Optional<Equipage> getById(Long id) {
        return equipageRepository.findById(id);
    }

    public Equipage save(Equipage equipage) {
        return equipageRepository.save(equipage);
    }

    public void delete(Long id) {
        equipageRepository.deleteById(id);
    }
}
