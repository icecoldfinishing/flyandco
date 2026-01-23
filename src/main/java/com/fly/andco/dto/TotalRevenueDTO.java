package com.fly.andco.dto;

import java.math.BigDecimal;

public class TotalRevenueDTO {
    private Long idVolInstance;
    private String aeroportDepart;
    private String aeroportArrivee;
    private String avion;
    private String dateDepart;
    private BigDecimal montantTicketsVendus;
    private BigDecimal montantPublicite;
    private BigDecimal montantTotal;

    public TotalRevenueDTO(Long idVolInstance, String aeroportDepart, String aeroportArrivee, 
                          String avion, String dateDepart, 
                          BigDecimal montantTicketsVendus,
                          BigDecimal montantPublicite, BigDecimal montantTotal) {
        this.idVolInstance = idVolInstance;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.avion = avion;
        this.dateDepart = dateDepart;
        this.montantTicketsVendus = montantTicketsVendus;
        this.montantPublicite = montantPublicite;
        this.montantTotal = montantTotal;
    }

    // Getters
    public Long getIdVolInstance() { return idVolInstance; }
    public String getAeroportDepart() { return aeroportDepart; }
    public String getAeroportArrivee() { return aeroportArrivee; }
    public String getAvion() { return avion; }
    public String getDateDepart() { return dateDepart; }
    public BigDecimal getMontantTicketsVendus() { return montantTicketsVendus; }
    public BigDecimal getMontantPublicite() { return montantPublicite; }
    public BigDecimal getMontantTotal() { return montantTotal; }

    // Setters
    public void setIdVolInstance(Long idVolInstance) { this.idVolInstance = idVolInstance; }
    public void setAeroportDepart(String aeroportDepart) { this.aeroportDepart = aeroportDepart; }
    public void setAeroportArrivee(String aeroportArrivee) { this.aeroportArrivee = aeroportArrivee; }
    public void setAvion(String avion) { this.avion = avion; }
    public void setDateDepart(String dateDepart) { this.dateDepart = dateDepart; }
    public void setMontantTicketsVendus(BigDecimal montantTicketsVendus) { this.montantTicketsVendus = montantTicketsVendus; }
    public void setMontantPublicite(BigDecimal montantPublicite) { this.montantPublicite = montantPublicite; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
}

