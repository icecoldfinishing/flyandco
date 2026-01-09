package com.fly.andco.service.passagers;

import com.fly.andco.model.passagers.Passager;
import com.fly.andco.repository.passagers.PassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassagerService {

    @Autowired
    private PassagerRepository passagerRepository;

    public List<Passager> getAll() {
        return passagerRepository.findAll();
    }

    public Optional<Passager> getById(Long id) {
        return passagerRepository.findById(id);
    }

    public Passager save(Passager passager) {
        return passagerRepository.save(passager);
    }

    public void delete(Long id) {
        passagerRepository.deleteById(id);
    }
}
