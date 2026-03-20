package com.example.stratecchallenge.domain;

import com.example.stratecchallenge.utils.Constants;

public class Planet {

    String name;
    double diameterKm;
    double massKg;


    public Planet(String name, double diameterKm, double massKg) {
        this.name = name;
        this.diameterKm = diameterKm;
        this.massKg = massKg;
    }

    public double getRadiusMeters() {
        return (diameterKm * 1000) / 2;
    }

    public double getEscapeVelocity() {
        double r = getRadiusMeters();
        return Math.sqrt((2 * Constants.G * massKg) / r);
    }

    public String getName() {
        return name;
    }

    public double getMassKg() {
        return massKg;
    }

    public double getDiameterKm() {
        return diameterKm;
    }


}
