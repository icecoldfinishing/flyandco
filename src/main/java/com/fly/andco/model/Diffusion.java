package com.fly.andco.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "diffusion")
public class Diffusion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diffusion")
    private Integer idDiffusion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @Column(nullable = false)
    private Integer nombre;
}
