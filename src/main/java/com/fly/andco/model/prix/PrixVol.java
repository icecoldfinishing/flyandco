package com.fly.andco.model.prix;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.model.compagnies.Compagnie;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prix_vol",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol", "classe"}))
public class PrixVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrix;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    private String classe;
    private Double prix;
    private LocalDateTime dateMaj;

    // getters & setters
    public Long getIdPrix() {
        return idPrix;
    }

    public void setIdPrix(Long idPrix) {
        this.idPrix = idPrix;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }
}
