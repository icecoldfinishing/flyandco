package com.fly.andco.service.compagnies;

import com.fly.andco.model.compagnies.Compagnie;
import com.fly.andco.repository.compagnies.CompagnieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompagnieService {
    @Autowired
    private CompagnieRepository compagnieRepository;

    public List<Compagnie> getAllCompagnies() {
        return compagnieRepository.findAll();
    }
}
