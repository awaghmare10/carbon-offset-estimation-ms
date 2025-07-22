package com.db.hackathon.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "carbon.factor")
public class CarbonFactorConfig {
    private double fertilizer;
    private double fuel;
    private double gwpN2o;
    private double irrigationFlood;
    private double irrigationOther;
    private double trees;
    private double biochar;
    private double pricePerTonne;
    private String currency;
    private double noTillReductionFactor;
    private double manureEmissionAddition;


    public double getFertilizer() { return fertilizer; }
    public void setFertilizer(double fertilizer) { this.fertilizer = fertilizer; }

    public double getFuel() { return fuel; }
    public void setFuel(double fuel) { this.fuel = fuel; }

    public double getGwpN2o() { return gwpN2o; }
    public void setGwpN2o(double gwpN2o) { this.gwpN2o = gwpN2o; }

    public double getIrrigationFlood() { return irrigationFlood; }
    public void setIrrigationFlood(double irrigationFlood) { this.irrigationFlood = irrigationFlood; }

    public double getIrrigationOther() { return irrigationOther; }
    public void setIrrigationOther(double irrigationOther) { this.irrigationOther = irrigationOther; }

    public double getTrees() { return trees; }
    public void setTrees(double trees) { this.trees = trees; }

    public double getBiochar() { return biochar; }
    public void setBiochar(double biochar) { this.biochar = biochar; }

    public double getPricePerTonne() { return pricePerTonne; }
    public void setPricePerTonne(double pricePerTonne) { this.pricePerTonne = pricePerTonne; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getNoTillReductionFactor() { return noTillReductionFactor; }
    public void setNoTillReductionFactor(double noTillReductionFactor) { this.noTillReductionFactor = noTillReductionFactor; }

    public double getManureEmissionAddition() { return manureEmissionAddition; }
    public void setManureEmissionAddition(double manureEmissionAddition) { this.manureEmissionAddition = manureEmissionAddition;  }
}
