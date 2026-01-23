package com.fly.andco.dto;
import java.math.BigDecimal;

public class RevenuePublicite {
    private Integer idSociete;
    private String societeNom;
    private int totalDiffusions;
    private BigDecimal montantUnitaire;
    private BigDecimal totalRevenue;
    private BigDecimal totalPaye;
    private BigDecimal resteAPayer;
    private BigDecimal pourcentageResteAPayer;
    private BigDecimal pourcentagePaye;
    
    public RevenuePublicite(Integer idSociete, String societeNom, int totalDiffusions, BigDecimal montantUnitaire, BigDecimal totalRevenue, BigDecimal totalPaye, BigDecimal resteAPayer) {
        this.idSociete = idSociete;
        this.societeNom = societeNom;
        this.totalDiffusions = totalDiffusions;
        this.montantUnitaire = montantUnitaire;
        this.totalRevenue = totalRevenue;
        this.totalPaye = totalPaye;
        this.resteAPayer = resteAPayer;
        if (totalRevenue != null && totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            this.pourcentageResteAPayer = resteAPayer.multiply(new BigDecimal(100)).divide(totalRevenue, 2, java.math.RoundingMode.HALF_UP);
            this.pourcentagePaye = totalPaye.multiply(new BigDecimal(100)).divide(totalRevenue, 2, java.math.RoundingMode.HALF_UP);
        } else {
            this.pourcentageResteAPayer = BigDecimal.ZERO;
            this.pourcentagePaye = BigDecimal.ZERO;
        }
    }

    public Integer getIdSociete() {
        return idSociete;
    }
    public void setIdSociete(Integer idSociete) {
        this.idSociete = idSociete;
    }
    public String getSocieteNom() {
        return societeNom;
    }
    public void setSocieteNom(String societeNom) {
        this.societeNom = societeNom;
    }
    public int getTotalDiffusions() {
        return totalDiffusions;
    }
    public void setTotalDiffusions(int totalDiffusions) {
        this.totalDiffusions = totalDiffusions;
    }
    public BigDecimal getMontantUnitaire() {
        return montantUnitaire;
    }
    public void setMontantUnitaire(BigDecimal montantUnitaire) {
        this.montantUnitaire = montantUnitaire;
    }
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public BigDecimal getTotalPaye() {
        return totalPaye;
    }
    public void setTotalPaye(BigDecimal totalPaye) {
        this.totalPaye = totalPaye;
    }
    public BigDecimal getResteAPayer() {
        return resteAPayer;
    }
    public void setResteAPayer(BigDecimal resteAPayer) {
        this.resteAPayer = resteAPayer;
    }
    public BigDecimal getPourcentageResteAPayer() {
        return pourcentageResteAPayer;
    }
    public void setPourcentageResteAPayer(BigDecimal pourcentageResteAPayer) {
        this.pourcentageResteAPayer = pourcentageResteAPayer;
    }
    public BigDecimal getPourcentagePaye() {
        return pourcentagePaye;
    }
    public void setPourcentagePaye(BigDecimal pourcentagePaye) {
        this.pourcentagePaye = pourcentagePaye;
    }
}