package com.biblio.bibliotheque.model.pret;

import jakarta.persistence.*;

@Entity
public class RegleJourApresRendu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_jour", nullable = false)
    private Integer nombreJour;

    // Constructeurs
    public RegleJourApresRendu() {
    }

    public RegleJourApresRendu(Integer nombreJour) {
        this.nombreJour = nombreJour;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNombreJour() {
        return nombreJour;
    }

    public void setNombreJour(Integer nombreJour) {
        this.nombreJour = nombreJour;
    }
}
