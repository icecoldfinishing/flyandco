package com.fly.andco.model.produit;

import com.fly.andco.model.vols.VolInstance;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vente_produit")
public class VenteProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vente_produit")
    private Long idVenteProduit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_vente")
    private LocalDateTime dateVente;

    public VenteProduit() {}

    public Long getIdVenteProduit() {
        return idVenteProduit;
    }

    public void setIdVenteProduit(Long idVenteProduit) {
        this.idVenteProduit = idVenteProduit;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDateTime dateVente) {
        this.dateVente = dateVente;
    }
}
