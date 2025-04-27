package com.marcosjourney.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class GameMap implements Serializable {
    private List<Planet> planets;
    private Planet currentPlanet;

    public GameMap() {
        this.planets = new ArrayList<>();
        initializePlanets();
    }

    private void initializePlanets() {
        // Ajouter les planètes disponibles
        planets.add(new IgnisPlanet());
        planets.add(new FrostiaPlanet());
        planets.add(new SylvanaPlanet());
        planets.add(new StationSpatiale());
        
        // Définir la planète de départ
        if (!planets.isEmpty()) {
            currentPlanet = planets.get(0);
        }
    }

    public List<Planet> getPlanets() {
        return new ArrayList<>(planets);
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentPlanet(Planet planet) {
        if (planets.contains(planet)) {
            this.currentPlanet = planet;
        }
    }

    public Planet getPlanetByName(String name) {
        return planets.stream()
                     .filter(planet -> planet.getName().equalsIgnoreCase(name))
                     .findFirst()
                     .orElse(null);
    }
} 