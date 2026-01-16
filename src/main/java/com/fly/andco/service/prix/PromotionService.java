package com.fly.andco.service.prix;

import com.fly.andco.model.prix.Promotion;
import com.fly.andco.repository.prix.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public List<Promotion> getAll() {
        return promotionRepository.findAll();
    }

    public Optional<Promotion> getById(Long id) {
        return promotionRepository.findById(id);
    }

    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }
}
