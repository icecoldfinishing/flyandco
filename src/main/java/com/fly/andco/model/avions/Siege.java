package com.fly.andco.model.avions;

import jakarta.persistence.*;

@Entity
@Table(name = "siege")
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSiege;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private com.fly.andco.model.vols.Vol vol;

    @Column(nullable = false, length = 5)
    private String numero;

    @Column(nullable = false, length = 20)
    private String classe;

    // =====================
    // Getters & Setters
    // =====================

    public Long getIdSiege() {
        return idSiege;
    }

    public void setIdSiege(Long idSiege) {
        this.idSiege = idSiege;
    }

    public com.fly.andco.model.vols.Vol getVol() {
        return vol;
    }

    public void setVol(com.fly.andco.model.vols.Vol vol) {
        this.vol = vol;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    // =====================
    // Helper pour Spring Data JPA Repository
    // =====================
    // Ceci permet de faire findByVol_IdVol(Long id) correctement
    public Long getVolId() {
        return vol != null ? vol.getIdVol() : null;
    }
}
