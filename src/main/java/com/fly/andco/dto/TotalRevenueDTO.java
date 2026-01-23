package com.fly.andco.dto;

import java.math.BigDecimal;

public class TotalRevenueDTO {
    private Long idVolInstance;
    private String aeroportDepart;
    private String aeroportArrivee;
    private String avion;
    private String dateDepart;
    private BigDecimal montantTicketsVendus;
    private BigDecimal montantPublicite; // Paid
    private BigDecimal montantPubliciteTotal; // Due
    private BigDecimal montantPubliciteReste; // Remaining
    private BigDecimal montantTotal; // Tickets + Pub Total
    private BigDecimal pourcentagePayePublicite;
    private BigDecimal pourcentageRestePublicite;

    public TotalRevenueDTO(Long idVolInstance, String aeroportDepart, String aeroportArrivee, 
                          String avion, String dateDepart, 
                          BigDecimal montantTicketsVendus,
                          BigDecimal montantPublicite, BigDecimal montantPubliciteTotal, BigDecimal montantTotal) {
        this.idVolInstance = idVolInstance;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.avion = avion;
        this.dateDepart = dateDepart;
        this.montantTicketsVendus = montantTicketsVendus;
        this.montantPublicite = montantPublicite;
        this.montantPubliciteTotal = montantPubliciteTotal;
        this.montantPubliciteReste = montantPubliciteTotal.subtract(montantPublicite);
        this.montantTotal = montantTotal;
        
        if (montantPubliciteTotal != null && montantPubliciteTotal.compareTo(BigDecimal.ZERO) > 0) {
            this.pourcentagePayePublicite = montantPublicite.multiply(new BigDecimal(100)).divide(montantPubliciteTotal, 2, java.math.RoundingMode.HALF_UP);
            this.pourcentageRestePublicite = new BigDecimal(100).subtract(this.pourcentagePayePublicite);
        } else {
            this.pourcentagePayePublicite = BigDecimal.ZERO;
            this.pourcentageRestePublicite = BigDecimal.ZERO;
        }
    }

    // Getters
    public Long getIdVolInstance() { return idVolInstance; }
    public String getAeroportDepart() { return aeroportDepart; }
    public String getAeroportArrivee() { return aeroportArrivee; }
    public String getAvion() { return avion; }
    public String getDateDepart() { return dateDepart; }
    public BigDecimal getMontantTicketsVendus() { return montantTicketsVendus; }
    public BigDecimal getMontantPublicite() { return montantPublicite; }
    public BigDecimal getMontantPubliciteTotal() { return montantPubliciteTotal; }
    public BigDecimal getMontantPubliciteReste() { return montantPubliciteReste; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public BigDecimal getPourcentagePayePublicite() { return pourcentagePayePublicite; }
    public BigDecimal getPourcentageRestePublicite() { return pourcentageRestePublicite; }

    // Setters
    public void setIdVolInstance(Long idVolInstance) { this.idVolInstance = idVolInstance; }
    public void setAeroportDepart(String aeroportDepart) { this.aeroportDepart = aeroportDepart; }
    public void setAeroportArrivee(String aeroportArrivee) { this.aeroportArrivee = aeroportArrivee; }
    public void setAvion(String avion) { this.avion = avion; }
    public void setDateDepart(String dateDepart) { this.dateDepart = dateDepart; }
    public void setMontantTicketsVendus(BigDecimal montantTicketsVendus) { this.montantTicketsVendus = montantTicketsVendus; }
    public void setMontantPublicite(BigDecimal montantPublicite) { this.montantPublicite = montantPublicite; }
    public void setMontantPubliciteTotal(BigDecimal montantPubliciteTotal) { this.montantPubliciteTotal = montantPubliciteTotal; }
    public void setMontantPubliciteReste(BigDecimal montantPubliciteReste) { this.montantPubliciteReste = montantPubliciteReste; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
    public void setPourcentagePayePublicite(BigDecimal pourcentagePayePublicite) { this.pourcentagePayePublicite = pourcentagePayePublicite; }
    public void setPourcentageRestePublicite(BigDecimal pourcentageRestePublicite) { this.pourcentageRestePublicite = pourcentageRestePublicite; }
}

