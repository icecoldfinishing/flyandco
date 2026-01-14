package com.fly.andco.service.paiements;

import com.fly.andco.model.paiements.Paiement;
import com.fly.andco.model.paiements.MoyenPaiement;
import com.fly.andco.repository.paiements.PaiementRepository;
import com.fly.andco.repository.paiements.MoyenPaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;


@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private MoyenPaiementRepository moyenPaiementRepository;

    /* =======================
        PAIEMENT
       ======================= */

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }

    /* =======================
        MOYEN DE PAIEMENT
       ======================= */

    public List<MoyenPaiement> getAllMoyensPaiement() {
        return moyenPaiementRepository.findAll();
    }

    public Optional<MoyenPaiement> getMoyenPaiementById(Long id) {
        return moyenPaiementRepository.findById(id);
    }

    public MoyenPaiement saveMoyenPaiement(MoyenPaiement moyenPaiement) {
        return moyenPaiementRepository.save(moyenPaiement);
    }

    public void deleteMoyenPaiement(Long id) {
        moyenPaiementRepository.deleteById(id);
    }

    public BigDecimal getTotalPaiementsByCompagnie(Long idCompagnie) {
        return paiementRepository.findAll().stream()
                .filter(p -> p.getReservation().getVolInstance()
                        .getAvion().getCompagnie().getIdCompagnie().equals(idCompagnie))
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
