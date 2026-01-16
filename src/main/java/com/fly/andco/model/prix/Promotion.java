package com.fly.andco.model.prix;

import jakarta.persistence.*;

@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromotion;

    @ManyToOne
    @JoinColumn(name = "id_prix", nullable = false)
    private PrixVol prixVol;

    @Column(name = "est_enfant", nullable = false)
    private Boolean estEnfant = false;

    private Double montant;

    // =====================
    // Getters & Setters
    // =====================

    public Long getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(Long idPromotion) {
        this.idPromotion = idPromotion;
    }

    public PrixVol getPrixVol() {
        return prixVol;
    }

    public void setPrixVol(PrixVol prixVol) {
        this.prixVol = prixVol;
    }

    public Boolean getEstEnfant() {
        return estEnfant;
    }

    public void setEstEnfant(Boolean estEnfant) {
        this.estEnfant = estEnfant;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}
