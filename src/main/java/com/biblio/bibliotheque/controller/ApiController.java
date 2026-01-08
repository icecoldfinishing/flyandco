package com.biblio.bibliotheque.controller;

import com.biblio.bibliotheque.model.livre.Livre;
import com.biblio.bibliotheque.model.livre.Exemplaire;
import com.biblio.bibliotheque.service.livre.LivreService;
import com.biblio.bibliotheque.service.livre.ExemplaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.biblio.bibliotheque.model.livre.*;
import com.biblio.bibliotheque.model.pret.*;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private LivreService livreService;
    
    @Autowired
    private ExemplaireService exemplaireService;


    @GetMapping("/livre")
    public Map<String, Object> getAllLivresWithExemplaires() {
        Map<String, Object> response = new HashMap<>();
        
        List<Livre> livres = livreService.getAllLivres();
        
        List<Map<String, Object>> livresWithExemplaires = livres.stream().map(livre -> {
            Map<String, Object> livreMap = new HashMap<>();
            livreMap.put("id", livre.getIdLivre());
            livreMap.put("titre", livre.getTitre());
            livreMap.put("auteur", livre.getAuteur());
            
            List<Exemplaire> exemplaires = livre.getExemplaires();
            
            List<Map<String, Object>> exemplairesList = exemplaires.stream().map(exemplaire -> {
                Map<String, Object> exemplaireMap = new HashMap<>();
                exemplaireMap.put("id_exemplaire", exemplaire.getId_exemplaire());
                exemplaireMap.put("code", exemplaire.getCode());
                exemplaireMap.put("disponible", exemplaireService.isExemplaireDisponible(exemplaire.getId_exemplaire()));
                return exemplaireMap;
            }).collect(Collectors.toList());
            
            livreMap.put("exemplaires", exemplairesList);
            return livreMap;
        }).collect(Collectors.toList());
        
        response.put("livres", livresWithExemplaires);
        return response;
    }

    @GetMapping("/livre/{id}")
    public Map<String, Object> getLivreByIdWithExemplaires(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Livre> livreOpt = livreService.getLivreById(id);
        
        if (livreOpt.isPresent()) {
            Livre livre = livreOpt.get();
            response.put("id", livre.getIdLivre());
            response.put("titre", livre.getTitre());
            response.put("auteur", livre.getAuteur());
            
            if (livre.getRestriction() != null) {
                response.put("restriction_age", livre.getRestriction().getAgeMin());
            }
            
            List<Exemplaire> exemplaires = livre.getExemplaires();
            
            List<Map<String, Object>> exemplairesList = exemplaires.stream().map(exemplaire -> {
                Map<String, Object> exemplaireMap = new HashMap<>();
                exemplaireMap.put("id_exemplaire", exemplaire.getId_exemplaire());
                exemplaireMap.put("code", exemplaire.getCode());
                exemplaireMap.put("disponible", exemplaireService.isExemplaireDisponible(exemplaire.getId_exemplaire()));
                return exemplaireMap;
            }).collect(Collectors.toList());
            
            response.put("exemplaires", exemplairesList);
        } else {
            response.put("message", "Livre non trouvé avec l'ID: " + id);
        }
        
        return response;
    }
}