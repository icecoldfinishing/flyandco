package com.fly.andco.model.aeroports;

import jakarta.persistence.*;

@Entity
@Table(name = "Aeroport")
public class Aeroport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAeroport;

    private String nom;
    private String ville;
    private String pays;

    @Column(length = 3, unique = true)
    private String codeIata;

    @Column(length = 4, unique = true)
    private String codeIcao;

    // getters & setters
}
