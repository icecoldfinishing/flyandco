package com.fly.andco.model.vols;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.model.aeroports.Aeroport;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vol")
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVol;

    @ManyToOne
    @JoinColumn(name = "id_compagnie", nullable = false)
    private com.fly.andco.model.compagnies.Compagnie compagnie;

    @ManyToOne
    @JoinColumn(name = "id_aeroport_depart", nullable = false)
    private Aeroport aeroportDepart;

    @ManyToOne
    @JoinColumn(name = "id_aeroport_arrivee", nullable = false)
    private Aeroport aeroportArrivee;


    // getters & setters
    public Long getIdVol() {
        return idVol;
    }

    public void setIdVol(Long idVol) {
        this.idVol = idVol;
    }

    public com.fly.andco.model.compagnies.Compagnie getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(com.fly.andco.model.compagnies.Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

}
