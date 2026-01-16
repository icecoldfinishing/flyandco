package com.fly.andco.model.passagers;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "passager")
public class Passager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passager")
    private Long idPassager;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "est_enfant", nullable = false)
    private boolean estEnfant = false; // par défaut FALSE

    // =====================
    // Getters & Setters
    // =====================

    public Long getIdPassager() {
        return idPassager;
    }

    public void setIdPassager(Long idPassager) {
        this.idPassager = idPassager;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEstEnfant() {
        return estEnfant;
    }

    public void setEstEnfant(boolean estEnfant) {
        this.estEnfant = estEnfant;
    }
}
