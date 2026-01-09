package com.fly.andco.service.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.repository.avions.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;

    public List<Avion> getAllAvions() {
        return avionRepository.findAll();
    }
}
