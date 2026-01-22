package com.fly.andco.model.publicite;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fly.andco.model.compagnies.Compagnie;

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

    public TarifPublicitaire() {}

    public Integer getIdTarifPub() {
        return idTarifPub;
    }

    public void setIdTarifPub(Integer idTarifPub) {
        this.idTarifPub = idTarifPub;
    }

    public Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
