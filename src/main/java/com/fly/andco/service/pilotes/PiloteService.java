package com.fly.andco.service.pilotes;

import com.fly.andco.model.pilotes.Pilote;
import com.fly.andco.repository.pilotes.PiloteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PiloteService {

    @Autowired
    private PiloteRepository piloteRepository;

    public List<Pilote> getAll() {
        return piloteRepository.findAll();
    }

    public Optional<Pilote> getById(Long id) {
        return piloteRepository.findById(id);
    }

    public Pilote save(Pilote pilote) {
        return piloteRepository.save(pilote);
    }

    public void delete(Long id) {
        piloteRepository.deleteById(id);
    }
}
