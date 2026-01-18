package com.fly.andco.model.prix;

import com.fly.andco.model.vols.VolInstance;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tarif_vol",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol_instance", "classe", "type_passager"}))
public class TarifVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarif;

    @ManyToOne
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @Column(nullable = false, length = 20)
    private String classe; // ECONOMY, PREMIUM, FIRST

    @Column(name = "type_passager", nullable = false, length = 20)
    private String typePassager; // ADULTE, ENFANT, BEBE

    @Column(nullable = false)
    private BigDecimal montant;

    // Getters & Setters
    public Long getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(Long idTarif) {
        this.idTarif = idTarif;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTypePassager() {
        return typePassager;
    }

    public void setTypePassager(String typePassager) {
        this.typePassager = typePassager;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
