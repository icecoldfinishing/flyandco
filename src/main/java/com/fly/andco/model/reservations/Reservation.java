package com.fly.andco.model.reservations;

import com.fly.andco.model.passagers.Passager;
import com.fly.andco.model.prix.PrixVol;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_passager", "id_prix_vol"}))
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @ManyToOne
    @JoinColumn(name = "id_prix_vol", nullable = false)
    private PrixVol prixVol;

    private LocalDateTime dateReservation;
    private String siege;
    private String statut;

    // getters & setters
}
