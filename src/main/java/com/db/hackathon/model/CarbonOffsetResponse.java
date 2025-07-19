package com.db.hackathon.model;

import java.util.Map;

public class CarbonOffsetResponse {
    public double estimatedOffsetTCO2e;
    public double estimatedCreditValue;
    public String currency;
    public Map<String, Double> emissions;
    public Map<String, Double> sequestration;
    public double leakage;
    public double uncertaintyBuffer;
}