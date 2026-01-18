package com.fly.andco.model.reservations;

import com.fly.andco.model.passagers.Passager;
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
    @JoinColumn(name = "id_tarif", nullable = false)
    private com.fly.andco.model.prix.TarifVol tarifVol;

    // Lien vers le siège spécifique (dépend now de siege_vol)
    @OneToOne
    @JoinColumn(name = "id_siege_vol", nullable = false, unique = true)
    private com.fly.andco.model.vols.SiegeVol siegeVol;

    private LocalDateTime dateReservation;
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

    public com.fly.andco.model.prix.TarifVol getTarifVol() {
        return tarifVol;
    }

    public void setTarifVol(com.fly.andco.model.prix.TarifVol tarifVol) {
        this.tarifVol = tarifVol;
    }

    public com.fly.andco.model.vols.SiegeVol getSiegeVol() {
        return siegeVol;
    }

    public void setSiegeVol(com.fly.andco.model.vols.SiegeVol siegeVol) {
        this.siegeVol = siegeVol;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
