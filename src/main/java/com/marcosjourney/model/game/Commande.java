package com.marcosjourney.model.game;

import java.util.ArrayList;
import java.util.List;

public enum Commande {
    NORD("n", "aller au nord"),
    SUD("s", "aller au sud"),
    EST("e", "aller à l'est"),
    OUEST("o", "aller à l'ouest"),
    AIDE("?", "afficher l'aide"),
    QUITTER("q", "quitter le jeu");

    private String abreviation;
    private String description;

    Commande(String abreviation, String description) {
        this.abreviation = abreviation;
        this.description = description;
    }

    public String toString() {
        return abreviation;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> toutesLesAbreviations() {
        List<String> abreviations = new ArrayList<>();
        for (Commande cmd : values()) {
            abreviations.add(cmd.abreviation);
        }
        return abreviations;
    }

    public static List<String> toutesLesNoms() {
        List<String> noms = new ArrayList<>();
        for (Commande cmd : values()) {
            noms.add(cmd.name());
        }
        return noms;
    }
} 