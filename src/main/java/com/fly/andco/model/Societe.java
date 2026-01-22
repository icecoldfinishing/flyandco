package com.fly.andco.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "societe")
public class Societe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_societe")
    private Integer idSociete;

    @Column(nullable = false, unique = true)
    private String nom;
}
