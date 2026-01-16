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

    @Column(name = "type_passager", nullable = false)
    private String typePassager;

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

    public String getTypePassager() {
        return typePassager;
    }

    public void setTypePassager(String typePassager) {
        this.typePassager = typePassager;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}
