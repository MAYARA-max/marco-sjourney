package com.marcosjourney.model.map;

public enum ZoneEffect {
    NONE("Aucun effet"),
    POISON("Empoisonnement"),
    HEALING("Guérison"),
    DAMAGE("Dégâts"),
    COLD("Froid extrême");

    private final String description;

    ZoneEffect(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
