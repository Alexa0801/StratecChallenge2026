package com.example.stratecchallenge.domain;

public class Rocket {

    private int engines;
    private double accelerationPerEngine; // m/s^2

    public Rocket(int engines, double accelerationPerEngine) {
        this.engines = engines;
        this.accelerationPerEngine = accelerationPerEngine;
    }

    public double getTotalAcceleration() {
        return engines * accelerationPerEngine;
    }

    public int getEngines() {
        return engines;
    }

    public double getAccelerationPerEngine() {
        return accelerationPerEngine;
    }
}
