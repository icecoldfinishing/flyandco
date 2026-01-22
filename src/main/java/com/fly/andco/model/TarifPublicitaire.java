package com.fly.andco.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tarif_publicitaire")
public class TarifPublicitaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif_pub")
    private Integer idTarifPub;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private Compagnie compagnie;

    @Column(nullable = false)
    private BigDecimal montant;
}
