package com.fly.andco.model.avions;

import jakarta.persistence.*;

@Entity
@Table(name = "siege")
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSiege;

    @ManyToOne
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

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

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
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
}
