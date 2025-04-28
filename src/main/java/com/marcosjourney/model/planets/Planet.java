package com.marcosjourney.model.planets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.marcosjourney.model.Element;
import com.marcosjourney.model.map.Zone;

public abstract class Planet implements Serializable {
    private String name;
    private String description;
    private Element element;
    private List<Zone> zones;
    private Zone startingZone;

    public Planet(String name, String description, Element element) {
        this.name = name;
        this.description = description;
        this.element = element;
        this.zones = new ArrayList<>();
        initializeZones();
    }

    protected abstract void initializeZones();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Element getElement() {
        return element;
    }

    public List<Zone> getZones() {
        return new ArrayList<>(zones);
    }

    public Zone getStartingZone() {
        return startingZone;
    }

    protected void setStartingZone(Zone zone) {
        if (zones.contains(zone)) {
            this.startingZone = zone;
        }
    }

    protected void addZone(Zone zone) {
        zones.add(zone);
    }

    public Zone getZoneByName(String zoneName) {
        return zones.stream()
                .filter(zone -> zone.getName().equalsIgnoreCase(zoneName))
                .findFirst()
                .orElse(null);
    }
}