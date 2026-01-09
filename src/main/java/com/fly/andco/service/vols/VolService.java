package com.fly.andco.service.vols;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.repository.vols.VolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolService {

    @Autowired
    private VolRepository volRepository;

    public List<Vol> getAll() {
        return volRepository.findAll();
    }

    public Optional<Vol> getById(Long id) {
        return volRepository.findById(id);
    }

    public Vol save(Vol vol) {
        return volRepository.save(vol);
    }

    public void delete(Long id) {
        volRepository.deleteById(id);
    }
}
