package com.fly.andco.model.prix;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.model.compagnies.Compagnie;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PrixVol",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol", "id_compagnie", "classe"}))
public class PrixVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrix;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private Compagnie compagnie;

    private String classe;
    private Double prix;
    private LocalDateTime dateMaj;

    // getters & setters
}
