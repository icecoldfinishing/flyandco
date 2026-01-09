package com.fly.andco.model.reservations;

import com.fly.andco.model.passagers.Passager;
import com.fly.andco.model.prix.PrixVol;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_passager", "id_vol_instance"}))
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @ManyToOne
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private com.fly.andco.model.vols.VolInstance volInstance;

    @ManyToOne
    @JoinColumn(name = "id_prix", nullable = false)
    private PrixVol prixVol;

    private LocalDateTime dateReservation;
    private String siege;
    private String statut;

    // getters & setters
    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public com.fly.andco.model.vols.VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(com.fly.andco.model.vols.VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public PrixVol getPrixVol() {
        return prixVol;
    }

    public void setPrixVol(PrixVol prixVol) {
        this.prixVol = prixVol;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getSiege() {
        return siege;
    }

    public void setSiege(String siege) {
        this.siege = siege;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
