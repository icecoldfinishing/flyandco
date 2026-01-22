package com.fly.andco.model.publicite;

import jakarta.persistence.*;

@Entity
@Table(name = "societe")
public class Societe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_societe")
    private Integer idSociete;

    @Column(nullable = false, unique = true)
    private String nom;

    public Societe() {}

    public Integer getIdSociete() {
        return idSociete;
    }

    public void setIdSociete(Integer idSociete) {
        this.idSociete = idSociete;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
