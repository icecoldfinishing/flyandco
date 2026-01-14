package com.fly.andco.model.compagnies;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "view_chiffre_affaire") // Nom de la vue
public class ViewChiffreAffaire {

    @Id
    @Column(name = "id_paiement")
    private Long idPaiement;

    @Column(name = "id_compagnie")
    private Long idCompagnie;

    @Column(name = "nom_compagnie")
    private String nomCompagnie;

    @Column(name = "id_avion")
    private Long idAvion;

    @Column(name = "avion_modele")
    private String avionModele;

    @Column(name = "numero_immatriculation")
    private String numeroImmatriculation;

    @Column(name = "id_vol")
    private Long idVol;

    @Column(name = "id_vol_instance")
    private Long idVolInstance;

    @Column(name = "date_depart")
    private LocalDateTime dateDepart;

    @Column(name = "date_arrivee")
    private LocalDateTime dateArrivee;

    @Column(name = "montant_paye")
    private BigDecimal montantPaye;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "statut_paiement")
    private String statutPaiement;

    // Getters & Setters
    // ... (générer tous les getters et setters)

    public Long getIdPaiement() {
        return this.idPaiement;
    }

    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Long getIdCompagnie() {
        return this.idCompagnie;
    }

    public void setIdCompagnie(Long idCompagnie) {
        this.idCompagnie = idCompagnie;
    }

    public String getNomCompagnie() {
        return this.nomCompagnie;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public Long getIdAvion() {
        return this.idAvion;
    }

    public void setIdAvion(Long idAvion) {
        this.idAvion = idAvion;
    }

    public String getAvionModele() {
        return this.avionModele;
    }

    public void setAvionModele(String avionModele) {
        this.avionModele = avionModele;
    }

    public String getNumeroImmatriculation() {
        return this.numeroImmatriculation;
    }

    public void setNumeroImmatriculation(String numeroImmatriculation) {
        this.numeroImmatriculation = numeroImmatriculation;
    }

    public Long getIdVol() {
        return this.idVol;
    }

    public void setIdVol(Long idVol) {
        this.idVol = idVol;
    }

    public Long getIdVolInstance() {
        return this.idVolInstance;
    }

    public void setIdVolInstance(Long idVolInstance) {
        this.idVolInstance = idVolInstance;
    }

    public LocalDateTime getDateDepart() {
        return this.dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateArrivee() {
        return this.dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public BigDecimal getMontantPaye() {
        return this.montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public LocalDateTime getDatePaiement() {
        return this.datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getStatutPaiement() {
        return this.statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
    
}
