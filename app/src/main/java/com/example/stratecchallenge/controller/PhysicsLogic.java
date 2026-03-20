package com.example.stratecchallenge.controller;

import android.content.Context;

import com.example.stratecchallenge.domain.Planet;
import com.example.stratecchallenge.domain.Rocket;
import com.example.stratecchallenge.utils.Constants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PhysicsLogic {
    public StringBuilder calculateEscapeVeolocities(List<Planet> planets) {
        StringBuilder result = new StringBuilder();

        for (Planet p : planets) {
            double vEscape = p.getEscapeVelocity();

            result.append(p.getName())
                    .append(": ")
                    .append(String.format("%.2f km/s", vEscape / 1000))
                    .append("\n");
        }

        return result;
    }

    public StringBuilder calculateTimeToReachSpace(Context context, List<Planet> planets, Rocket rocket) {
        StringBuilder result = new StringBuilder();


        double totalAcceleration = rocket.getTotalAcceleration();

        for (Planet planet : planets) {

            double escapeVelocity = planet.getEscapeVelocity();

            double timeToEscape = escapeVelocity / totalAcceleration;
            double distanceToEscape = 0.5 * totalAcceleration * Math.pow(timeToEscape, 2);
            double distanceFromCenter = planet.getRadiusMeters() + distanceToEscape;

            result.append(planet.getName())
                    .append(": Time to escape = ")
                    .append(String.format("%.2f seconds", timeToEscape))
                    .append(", Distance from center = ")
                    .append(String.format("%.2f km", distanceFromCenter / 1000))
                    .append("\n");
        }

        return result;
    }

}
