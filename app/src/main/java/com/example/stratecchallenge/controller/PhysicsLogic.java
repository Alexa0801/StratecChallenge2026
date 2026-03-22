package com.example.stratecchallenge.controller;

import android.content.Context;

import com.example.stratecchallenge.domain.Planet;
import com.example.stratecchallenge.domain.Rocket;
import com.example.stratecchallenge.domain.SolarPlanet;
import com.example.stratecchallenge.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public StringBuilder calculateTimeToEscape(List<Planet> planets, Rocket rocket) {
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

    public StringBuilder calculateJourney(SolarPlanet start, SolarPlanet dest, Rocket rocket) {
        StringBuilder result = new StringBuilder();
        double vCruise = Math.max(start.getEscapeVelocity(), dest.getEscapeVelocity());

        double a = rocket.getTotalAcceleration();

        double tAccel = vCruise / a;

        double dAccel = 0.5 * a * tAccel * tAccel;
        double dAccelKm = dAccel / 1000.0;

        double tDecel = tAccel;
        double dDecelKm = dAccelKm;

        double dTotalCenters = Math.abs(dest.getOrbitalRadiusKm() - start.getOrbitalRadiusKm());

        double dCruiseKm = dTotalCenters
                - dAccelKm
                - dDecelKm
                - (start.getRadiusMeters() / 1000.0)
                - (dest.getRadiusMeters() / 1000.0);

        double tCruise = (dCruiseKm * 1000) / vCruise;

        double tTotal = tAccel + tCruise + tDecel;

        int totalSec = (int) Math.round(tTotal);
        int days = totalSec / 86400;
        int hours = (totalSec % 86400) / 3600;
        int minutes = (totalSec % 3600) / 60;
        int seconds = totalSec % 60;

        result.append("Journey from ").append(start.getName()).append(" to ").append(dest.getName()).append("\n\n");
        result.append(String.format("Time to reach cruising velocity: %.2f s\n", tAccel));
        result.append(String.format("Distance from %s surface when reaching speed: %.2f km\n", start.getName(), dAccelKm));
        result.append(String.format("Cruising distance: %.2f km\n", dCruiseKm));
        result.append(String.format("Time to cruise: %.2f s\n", tCruise));
        result.append(String.format("Distance from %s surface to start deceleration: %.2f km\n", dest.getName(), dDecelKm));
        result.append(String.format("Time to decelerate: %.2f s\n", tDecel));
        result.append(String.format("Total travel time: %.0f s (~%d days, %d h, %d min, %d sec)\n", tTotal, days, hours, minutes, seconds));

        return result;
    }

    public StringBuilder calculatePositions(List<SolarPlanet> planets, double days) {
        StringBuilder result = new StringBuilder();

        for (SolarPlanet planet : planets) {
            double angle = planet.calculateAngle(planet, days);
            result.append(planet.getName())
                    .append(": ")
                    .append(String.format("%.2f", angle))
                    .append("°\n");
        }
        return result;
    }

    public StringBuilder calculateOptimalTransfer(List<SolarPlanet> planets, SolarPlanet start, SolarPlanet dest, Rocket rocket) {
        StringBuilder result = new StringBuilder();

        double startTimeDays = 100 * 365.0;
        double maxSearchDays = 10 * 365.0;

        double bestTime = -1;
        double bestDistance = Double.MAX_VALUE;

        for (double t = startTimeDays; t <= startTimeDays + maxSearchDays; t++) {

            // positions for all planets
            Map<SolarPlanet, double[]> positions = new HashMap<>();

            for (SolarPlanet p : planets) {
                double angle = (t % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
                double angleRad = Math.toRadians(angle);

                double rKm = p.getOrbitalRadiusAU() * Constants.AU_KM;

                double x = rKm * Math.cos(angleRad);
                double y = rKm * Math.sin(angleRad);

                positions.put(p, new double[]{x, y});
            }

            double[] startPos = positions.get(start);
            double[] destPos = positions.get(dest);

            double x1 = startPos[0];
            double y1 = startPos[1];
            double x2 = destPos[0];
            double y2 = destPos[1];

            // distance between planets
            double dx = x2 - x1;
            double dy = y2 - y1;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // collision check
            boolean collision = false;

            for (SolarPlanet p : planets) {
                if (p == start || p == dest)
                    continue;

                double[] pos = positions.get(p);
                double px = pos[0];
                double py = pos[1];

                double numerator = Math.abs(
                        (y2 - y1) * px - (x2 - x1) * py + x2 * y1 - y2 * x1
                );

                double denominator = Math.sqrt(
                        Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2)
                );

                double distanceToLine = numerator / denominator;

                double planetRadiusKm = p.getDiameterKm() / 2.0;

                if (distanceToLine < planetRadiusKm) {
                    collision = true;
                    break;
                }
            }

            if (!collision && distance < bestDistance) {
                bestDistance = distance;
                bestTime = t;
            }
        }

        if (bestTime == -1) {
            result.append("No valid transfer window found within 10 years.");
            return result;
        }

        double waitDays = bestTime - startTimeDays;

        Map<SolarPlanet, double[]> finalPositions = new HashMap<>();

        for (SolarPlanet p : planets) {
            double angle = (bestTime % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
            double angleRad = Math.toRadians(angle);

            double rKm = p.getOrbitalRadiusAU() * Constants.AU_KM;

            double x = rKm * Math.cos(angleRad);
            double y = rKm * Math.sin(angleRad);

            finalPositions.put(p, new double[]{x, y});
        }

        result.append("Optimal transfer window found!\n");
        result.append(String.format("Wait time: %.2f days\n\n", waitDays));

        result.append("Planet positions:\n");
        for (SolarPlanet p : planets) {
            double angle = (bestTime % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
            result.append(p.getName())
                    .append(": ")
                    .append(String.format("%.2f °\n", angle));
        }

        result.append("\n");

        result.append(calculateJourney(start, dest, rocket));

        return result;
    }

    private boolean willCollideDuringFlight(List<SolarPlanet> planets, SolarPlanet start, SolarPlanet dest, double launchTime, Rocket rocket) {
        // positions at launch
        Map<SolarPlanet, double[]> startPositions = new HashMap<>();

        for (SolarPlanet p : planets) {
            double angle = (launchTime % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
            double rKm = p.getOrbitalRadiusAU() * Constants.AU_KM;

            double x = rKm * Math.cos(Math.toRadians(angle));
            double y = rKm * Math.sin(Math.toRadians(angle));

            startPositions.put(p, new double[]{x, y});
        }

        double[] startPos = startPositions.get(start);
        double[] destPos = startPositions.get(dest);

        double x1 = startPos[0];
        double y1 = startPos[1];
        double x2 = destPos[0];
        double y2 = destPos[1];

        // direction vector
        double dx = x2 - x1;
        double dy = y2 - y1;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        double dirX = dx / totalDistance;
        double dirY = dy / totalDistance;

        // get travel time from Stage 3
        double vEscapeStart = start.getEscapeVelocity();
        double vEscapeDest = dest.getEscapeVelocity();

        double cruiseVelocity = Math.max(vEscapeStart, vEscapeDest);

        double accel = rocket.getTotalAcceleration();

        double tBurn = cruiseVelocity / accel;
        double dBurn = 0.5 * accel * tBurn * tBurn;

        double cruiseDistance = totalDistance - 2 * dBurn;
        double tCruise = cruiseDistance / cruiseVelocity;

        double totalTime = 2 * tBurn + tCruise;

        // simulate
        double dt = 100; // seconds step

        for (double t = 0; t <= totalTime; t += dt) {

            double currentTime = launchTime + t / 86400.0; // convert seconds → days

            // rocket position
            double distanceTraveled;

            if (t < tBurn) {
                distanceTraveled = 0.5 * accel * t * t;
            } else if (t < tBurn + tCruise) {
                distanceTraveled = dBurn + cruiseVelocity * (t - tBurn);
            } else {
                double tDecel = t - (tBurn + tCruise);
                distanceTraveled = dBurn + cruiseDistance +
                        cruiseVelocity * tDecel - 0.5 * accel * tDecel * tDecel;
            }

            double rx = x1 + dirX * distanceTraveled;
            double ry = y1 + dirY * distanceTraveled;

            // check all planets
            for (SolarPlanet p : planets) {
                if (p == start || p == dest) continue;

                double angle = (currentTime % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
                double rKm = p.getOrbitalRadiusAU() * Constants.AU_KM;

                double px = rKm * Math.cos(Math.toRadians(angle));
                double py = rKm * Math.sin(Math.toRadians(angle));

                double dist = Math.sqrt(
                        Math.pow(px - rx, 2) + Math.pow(py - ry, 2)
                );

                double planetRadius = p.getDiameterKm() / 2.0;

                if (dist < planetRadius) {
                    return true; // collision
                }
            }
        }

        return false;
    }

    private double[] getPlanetPosition(SolarPlanet planet, double timeDays) {
        final double AU_TO_KM = 149597870.7;

        double angle = (timeDays % planet.getOrbitalPeriodDays())
                / planet.getOrbitalPeriodDays() * 360.0;

        double r = planet.getOrbitalRadiusAU() * AU_TO_KM;

        double x = r * Math.cos(Math.toRadians(angle));
        double y = r * Math.sin(Math.toRadians(angle));

        return new double[]{x, y};
    }

    private double distance(double[] a, double[] b) {
        return Math.sqrt(
                Math.pow(a[0] - b[0], 2) +
                        Math.pow(a[1] - b[1], 2)
        );
    }

    private boolean willCollide(List<SolarPlanet> planets, SolarPlanet start, SolarPlanet dest, double[] startPos, double[] destPos) {
        for (SolarPlanet p : planets) {
            if (p == start || p == dest) continue;

            double[] pos = getPlanetPosition(p, 0);

            double dist = distancePointToSegment(
                    pos, startPos, destPos
            );

            double radius = p.getDiameterKm() / 2.0;

            if (dist < radius) return true;
        }

        return false;
    }

    private double distancePointToSegment(double[] p, double[] a, double[] b) {
        double px = p[0], py = p[1];
        double x1 = a[0], y1 = a[1];
        double x2 = b[0], y2 = b[1];

        double dx = x2 - x1;
        double dy = y2 - y1;

        double t = ((px - x1) * dx + (py - y1) * dy)
                / (dx * dx + dy * dy);

        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        return Math.sqrt(
                Math.pow(px - closestX, 2) +
                        Math.pow(py - closestY, 2)
        );
    }

    public StringBuilder calculateOptimalTransferMoving(List<SolarPlanet> planets, SolarPlanet start, SolarPlanet dest, Rocket rocket) {
        StringBuilder result = new StringBuilder();

        double startTime = 365 * 100;
        double maxTime = startTime + 365 * 10;

        double bestTime = -1;
        double bestDistance = Double.MAX_VALUE;

        for (double t = startTime; t <= maxTime; t += 1) {

            double[] startPos = getPlanetPosition(start, t);
            double[] destPos = getPlanetPosition(dest, t);

            double distance = distance(startPos, destPos);

            boolean collisionNow = willCollide(planets, start, dest, startPos, destPos);

            boolean collisionFuture = willCollideDuringFlight(
                    planets, start, dest, t, rocket
            );

            if (!collisionNow && !collisionFuture && distance < bestDistance) {
                bestDistance = distance;
                bestTime = t;
            }
        }

        if (bestTime == -1) {
            result.append("No valid transfer window found in 10 years.\n");
            return result;
        }

        double[] startPos = getPlanetPosition(start, bestTime);
        double[] destPos = getPlanetPosition(dest, bestTime);

        double totalDistance = distance(startPos, destPos);

        double vStart = start.getEscapeVelocity();
        double vDest = dest.getEscapeVelocity();

        double cruiseVelocity = Math.max(vStart, vDest);
        double accel = rocket.getTotalAcceleration();

        double tBurn = cruiseVelocity / accel;
        double dBurn = 0.5 * accel * tBurn * tBurn;

        double cruiseDistance = totalDistance - 2 * dBurn;
        double tCruise = cruiseDistance / cruiseVelocity;

        double totalTime = 2 * tBurn + tCruise;

        result.append("Optimal transfer window found!\n");
        result.append(String.format("Wait time: %.2f days\n\n", bestTime - startTime));

        result.append("Planet positions:\n");
        for (SolarPlanet p : planets) {
            double angle = (bestTime % p.getOrbitalPeriodDays()) / p.getOrbitalPeriodDays() * 360.0;
            result.append(p.getName())
                    .append(": ")
                    .append(String.format("%.2f °\n", angle));
        }

        result.append("\n");

        result.append("Journey from ").append(start.getName()).append(" to ").append(dest.getName()).append("\n\n");        result.append("Time to reach cruising velocity: ")
                .append(String.format("%.2f s\n", tBurn));

        result.append("Distance from start surface: ")
                .append(String.format("%.2f km\n", dBurn / 1000));

        result.append("Cruise time: ")
                .append(String.format("%.2f s\n", tCruise));

        result.append("Deceleration time: ")
                .append(String.format("%.2f s\n", tBurn));

        int totalSeconds = (int) totalTime;

        int days = totalSeconds / 86400;
        totalSeconds %= 86400;

        int hours = totalSeconds / 3600;
        totalSeconds %= 3600;

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        result.append("Total travel time: ")
                .append(days).append("d ")
                .append(hours).append("h ")
                .append(minutes).append("m ")
                .append(seconds).append("s\n");

        return result;
    }

}
