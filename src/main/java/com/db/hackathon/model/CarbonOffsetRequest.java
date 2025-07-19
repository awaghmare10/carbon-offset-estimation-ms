package com.db.hackathon.model;

public class CarbonOffsetRequest {
    public double areaHa;
    public String cropType;
    public double fertilizerNkgPerHa;
    public String irrigationType;
    public double dieselLiters;
    public boolean manureApplied;
    public boolean noTill;
    public int treesPlanted;
    public double biocharTons;
    public Location location;

    public static class Location {
        public double lat;
        public double lon;
    }
}