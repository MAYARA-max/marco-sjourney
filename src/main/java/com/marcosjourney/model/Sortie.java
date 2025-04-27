package com.marcosjourney.model;

public enum Sortie {
    NORD("nord"),
    SUD("sud"),
    EST("est"),
    OUEST("ouest"),
    HAUT("haut"),
    BAS("bas");

    private final String direction;

    Sortie(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return direction;
    }

    public static Sortie fromString(String direction) {
        String directionLower = direction.toLowerCase();
        for (Sortie sortie : values()) {
            if (sortie.direction.equals(directionLower)) {
                return sortie;
            }
        }
        throw new IllegalArgumentException("Direction invalide: " + direction);
    }
} 