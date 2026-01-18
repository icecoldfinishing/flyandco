package com.fly.andco.model.vols;

import com.fly.andco.model.avions.Siege;
import jakarta.persistence.*;

@Entity
@Table(name = "siege_vol",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_vol_instance", "id_siege"}))
public class SiegeVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSiegeVol;

    @ManyToOne
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @ManyToOne
    @JoinColumn(name = "id_siege", nullable = false)
    private Siege siege;

    @Column(length = 20)
    private String statut; // LIBRE, OCCUPE

    // Getters & Setters
    public Long getIdSiegeVol() {
        return idSiegeVol;
    }

    public void setIdSiegeVol(Long idSiegeVol) {
        this.idSiegeVol = idSiegeVol;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
