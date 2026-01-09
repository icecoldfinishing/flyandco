package com.fly.andco.model.equipages;

import com.fly.andco.model.vols.Vol;
import com.fly.andco.model.pilotes.Pilote;
import jakarta.persistence.*;

@Entity
@Table(name = "Equipage",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol", "id_pilote"}))
public class Equipage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipage;

    @ManyToOne
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @ManyToOne
    @JoinColumn(name = "id_pilote", nullable = false)
    private Pilote pilote;

    private String role;

    // getters & setters
}
