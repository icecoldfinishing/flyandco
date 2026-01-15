package com.fly.andco.dto;

import java.util.Map;

public class RevenueDetail {
    private String classe;
    private double prixUnitaire;
    private long nombreSieges;
    private double total;

    // Constructors, Getters, Setters
    public RevenueDetail(String classe, double prixUnitaire, long nombreSieges, double total) {
        this.classe = classe;
        this.prixUnitaire = prixUnitaire;
        this.nombreSieges = nombreSieges;
        this.total = total;
    }
    
    public String getClasse() { return classe; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public long getNombreSieges() { return nombreSieges; }
    public double getTotal() { return total; }
}
