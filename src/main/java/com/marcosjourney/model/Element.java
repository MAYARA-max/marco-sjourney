package com.marcosjourney.model;

public enum Element {
    NONE("Aucun"),
    FIRE("Feu"),
    WATER("Eau"),
    EARTH("Terre"),
    AIR("Air");

    private final String name;

    Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
} 