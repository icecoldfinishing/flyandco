package com.fly.andco.dto;
import java.math.BigDecimal;

public class RevenuePublicite {
    private String societeNom;
    private int totalDiffusions;
    private BigDecimal montantUnitaire;
    private BigDecimal totalRevenue;
    private BigDecimal totalPaye;
    private BigDecimal resteAPayer;
    
    public RevenuePublicite(String societeNom, int totalDiffusions, BigDecimal montantUnitaire, BigDecimal totalRevenue, BigDecimal totalPaye, BigDecimal resteAPayer) {
        this.societeNom = societeNom;
        this.totalDiffusions = totalDiffusions;
        this.montantUnitaire = montantUnitaire;
        this.totalRevenue = totalRevenue;
        this.totalPaye = totalPaye;
        this.resteAPayer = resteAPayer;
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
}