package com.fly.andco.model.paiements;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "moyen_paiement")
public class MoyenPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moyen_paiement")
    private Long idMoyenPaiement;

    @Column(nullable = false, unique = true)
    private String libelle;

    public Long getIdMoyenPaiement() {
        return idMoyenPaiement;
    }

    public void setIdMoyenPaiement(Long idMoyenPaiement) {
        this.idMoyenPaiement = idMoyenPaiement;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
