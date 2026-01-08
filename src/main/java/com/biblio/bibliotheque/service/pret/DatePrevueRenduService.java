package com.biblio.bibliotheque.service.pret;

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.bibliotheque.model.pret.DatePrevueRendu;
import com.biblio.bibliotheque.repository.pret.DatePrevueRenduRepository;

@Service
public class DatePrevueRenduService {

    @Autowired
    private DatePrevueRenduRepository datePrevueRenduRepository;

    // Récupérer tous les enregistrements
    public java.util.List<DatePrevueRendu> getAllDatesPrevues() {
        return datePrevueRenduRepository.findAll();
    }

    // Trouver par id
    public Optional<DatePrevueRendu> getById(Integer id) {
        return datePrevueRenduRepository.findById(id);
    }

    // Trouver par id du Pret
    public Optional<DatePrevueRendu> getByPretId(Integer idPret) {
        return datePrevueRenduRepository.findByPretId(idPret);
    }

    // Enregistrer ou modifier un enregistrement
    public DatePrevueRendu save(DatePrevueRendu datePrevueRendu) {
        return datePrevueRenduRepository.save(datePrevueRendu);
    }

    // Supprimer par id
    public void deleteById(Integer id) {
        datePrevueRenduRepository.deleteById(id);
    }
    public List<DatePrevueRendu> getAllDatesPrevuesByAdherentIdAndDate(Integer idAdherent, LocalDate dateMax) {
        return datePrevueRenduRepository.findAllByAdherentIdAndDatePrevueBeforeOrEqual(idAdherent, dateMax);
    }

}
