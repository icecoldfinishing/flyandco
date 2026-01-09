package com.fly.andco.service.aeroports;

import com.fly.andco.model.aeroports.Aeroport;
import com.fly.andco.repository.aeroports.AeroportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AeroportService {

    @Autowired
    private AeroportRepository aeroportRepository;

    public List<Aeroport> getAll() {
        return aeroportRepository.findAll();
    }

    public Optional<Aeroport> getById(Long id) {
        return aeroportRepository.findById(id);
    }

    public Aeroport save(Aeroport aeroport) {
        return aeroportRepository.save(aeroport);
    }

    public void delete(Long id) {
        aeroportRepository.deleteById(id);
    }
}
