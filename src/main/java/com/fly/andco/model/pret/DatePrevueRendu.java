package com.biblio.bibliotheque.model.pret;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class DatePrevueRendu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_date_prevue;

    @Column(name = "date_prevue", nullable = false)
    private LocalDate datePrevue;

    @OneToOne
    @JoinColumn(name = "id_pret", nullable = false, unique = true)
    private Pret pret;

    // Constructeurs
    public DatePrevueRendu() {
        // Constructeur vide
    }

    public DatePrevueRendu(LocalDate datePrevue, Pret pret) {
        this.datePrevue = datePrevue;
        this.pret = pret;
    }

    // Getters et Setters
    public Integer getId_date_prevue() {
        return id_date_prevue;
    }

    public void setId_date_prevue(Integer id_date_prevue) {
        this.id_date_prevue = id_date_prevue;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        this.datePrevue = datePrevue;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }
}
