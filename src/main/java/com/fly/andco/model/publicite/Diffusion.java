package com.fly.andco.model.publicite;

import jakarta.persistence.*;
import com.fly.andco.model.vols.VolInstance;
import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarif_pub", nullable = false)
    private TarifPublicitaire tarifPublicitaire;

    @Column(name = "date_diffusion", nullable = false)
    private LocalDate dateDiffusion;

    @Column(nullable = false)
    private Integer nombre;

    public Diffusion() {}

    public Integer getIdDiffusion() {
        return idDiffusion;
    }

    public void setIdDiffusion(Integer idDiffusion) {
        this.idDiffusion = idDiffusion;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public TarifPublicitaire getTarifPublicitaire() {
        return tarifPublicitaire;
    }

    public void setTarifPublicitaire(TarifPublicitaire tarifPublicitaire) {
        this.tarifPublicitaire = tarifPublicitaire;
    }

    public LocalDate getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(LocalDate dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }
}
