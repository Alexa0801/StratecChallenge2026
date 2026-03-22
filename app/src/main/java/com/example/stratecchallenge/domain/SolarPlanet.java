package com.example.stratecchallenge.domain;

import com.example.stratecchallenge.utils.Constants;

public class SolarPlanet extends Planet{
    private double orbitalRadiusAU;
    private double orbitalRadiusKm;
    private int orbitalPeriodDays;

    public SolarPlanet(String name, double diameterKm, double massKg,
                       double orbitalRadiusAU, int orbitalPeriodDays) {
        super(name, diameterKm, massKg);
        this.orbitalRadiusAU = orbitalRadiusAU;
        this.orbitalRadiusKm = orbitalRadiusAU * Constants.AU_KM;
        this.orbitalPeriodDays = orbitalPeriodDays;
    }

    public double getOrbitalRadiusKm() {
        return orbitalRadiusKm;
    }

    public int getOrbitalPeriodDays() {
        return orbitalPeriodDays;
    }

    public double calculateAngle(SolarPlanet planet, double days) {
        double angle = (360.0 / planet.getOrbitalPeriodDays()) * days;
        return angle % 360;
    }

    public double getOrbitalRadiusAU() {
        return orbitalRadiusAU;
    }

}
