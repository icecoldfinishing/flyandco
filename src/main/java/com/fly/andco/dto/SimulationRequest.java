package com.fly.andco.dto;

import java.util.List;
import java.util.ArrayList;

public class SimulationRequest {
    private List<SimulationItem> items = new ArrayList<>();

    public List<SimulationItem> getItems() {
        return items;
    }

    public void setItems(List<SimulationItem> items) {
        this.items = items;
    }

    public static class SimulationItem {
        private String classe;
        private Double prixAdulte;
        private Double prixEnfant; // Or promotion price
        private Integer promoCount; // Number of children/promo seats
        private Integer adultCount; // Number of normal seats

        private Double prixBebe;
        private Integer bebeCount;

        public String getClasse() { return classe; }
        public void setClasse(String classe) { this.classe = classe; }

        public Double getPrixAdulte() { return prixAdulte; }
        public void setPrixAdulte(Double prixAdulte) { this.prixAdulte = prixAdulte; }

        public Double getPrixEnfant() { return prixEnfant; }
        public void setPrixEnfant(Double prixEnfant) { this.prixEnfant = prixEnfant; }

        public Double getPrixBebe() { return prixBebe; }
        public void setPrixBebe(Double prixBebe) { this.prixBebe = prixBebe; }

        public Integer getPromoCount() { return promoCount; }
        public void setPromoCount(Integer promoCount) { this.promoCount = promoCount; }

        public Integer getAdultCount() { return adultCount; }
        public void setAdultCount(Integer adultCount) { this.adultCount = adultCount; }

        public Integer getBebeCount() { return bebeCount; }
        public void setBebeCount(Integer bebeCount) { this.bebeCount = bebeCount; }
        
        public double getTotal() {
            double pAdulte = (prixAdulte != null) ? prixAdulte : 0.0;
            double pEnfant = (prixEnfant != null) ? prixEnfant : 0.0;
            double pBebe = (prixBebe != null) ? prixBebe : 0.0;
            
            int cAdulte = (adultCount != null) ? adultCount : 0;
            int cEnfant = (promoCount != null) ? promoCount : 0;
            int cBebe = (bebeCount != null) ? bebeCount : 0;
            
            return (pAdulte * cAdulte) + (pEnfant * cEnfant) + (pBebe * cBebe);
        }
    }
}
