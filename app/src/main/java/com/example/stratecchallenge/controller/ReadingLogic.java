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

public class ReadingLogic {
    public List<Planet> readPlanets(Context context) {
        List<Planet> planets = new ArrayList<>();

        try {
            InputStream is = context.getAssets().open("Planetary_Data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0].trim();

                String diameterPart = parts[1].split(",")[0];
                double diameter = Double.parseDouble(
                        diameterPart.replaceAll("[^0-9.]", "")
                );

                String massPart = parts[1].split(",")[1];
                double mass = Double.parseDouble(
                        massPart.replaceAll("[^0-9.]", "")
                );

                if (name.equals("Earth")) {
                    mass = Constants.EARTH_MASS;
                } else {
                    mass *= Constants.EARTH_MASS;
                }

                planets.add(new Planet(name, diameter, mass));
            }

        } catch (Exception e) {
            System.out.println("Error reading planets: " + e.getMessage());
        }

        return planets;
    }

    public Rocket readRocketData(Context context) {
        int engines = 0;
        double acc = 0;

        try {
            InputStream is = context.getAssets().open("Rocket_Data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Number of rocket engines")) {
                    engines = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                }
                if (line.toLowerCase().contains("acceleration")) {
                    String numStr = line.replaceAll(".*?([0-9.]+).*", "$1");
                    acc = Double.parseDouble(numStr); // 10.0
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading rocket data: " + e.getMessage());
        }

        return new Rocket(engines, acc);
    }
}
