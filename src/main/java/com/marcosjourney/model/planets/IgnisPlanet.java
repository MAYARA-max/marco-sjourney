package com.marcosjourney.model.planets;

import com.marcosjourney.model.Element;
import com.marcosjourney.model.items.Item;
import com.marcosjourney.model.items.ItemType;
import com.marcosjourney.model.map.Sortie;
import com.marcosjourney.model.map.Zone;
import com.marcosjourney.model.player.Player;

public class IgnisPlanet extends Planet {
    private static final int FALLING_ROCK_TIMER = 10; // 10 secondes pour esquiver
    private static final int LAVA_DAMAGE = 5; // Dégâts par seconde dans la vallée de lave
    private static final int LAVA_DAMAGE_INTERVAL = 1000; // Intervalle de dégâts en millisecondes

    public IgnisPlanet() {
        super("Ignis", "Une planète volcanique aux températures extrêmes", Element.FIRE);
    }
    
    @Override
    protected void initializeZones() {
        // Création des zones
        Zone startingZone = new Zone("Zone de Débarquement", 
            "Vous êtes dans la zone de débarquement d'Ignis. La chaleur est intense ici.", 
            "/images/ignis_zone1.png");
        
        Zone lavaValley = new Zone("LavaValley", 
            "Vous êtes dans la Vallée de Lave. Des rivières de magma coulent autour de vous.", 
            "/images/ignis_zone2.jpg");
            
        Zone magmaCave = new Zone("MagmaCave", 
            "Vous êtes dans la Caverne de Magma. La chaleur est insupportable ici.", 
            "/images/ignis_zone3.jpg");
            
        // Ajout des zones à la planète
        addZone(startingZone);
        addZone(lavaValley);
        addZone(magmaCave);
        
        // Configuration des sorties
        startingZone.addExit(Sortie.NORD, lavaValley);
        lavaValley.addExit(Sortie.SUD, startingZone);
        lavaValley.addExit(Sortie.EST, magmaCave);
        magmaCave.addExit(Sortie.OUEST, lavaValley);
        
        // Verrouillage initial de la Caverne de Magma
        magmaCave.setLocked(true);
        
        // Zone de départ
        setStartingZone(startingZone);
        
        System.out.println("Debug - Zones initialisées:");
        System.out.println("- " + startingZone.getName() + " (PNJs: " + startingZone.getPNJs().size() + ")");
        System.out.println("- " + lavaValley.getName() + " (PNJs: " + lavaValley.getPNJs().size() + ")");
        System.out.println("- " + magmaCave.getName() + " (PNJs: " + magmaCave.getPNJs().size() + ")");
    }

    public boolean checkFallingRock(Player player) {
        // Implémentation de la logique pour esquiver la pierre
        // Retourne true si le joueur a réussi à esquiver
        return player.getIntelligence() > 5; // Exemple simple
    }

    public boolean checkLavaShelter(Player player) {
        // Implémentation de la logique pour se mettre à l'abri
        // Retourne true si le joueur a trouvé un abri efficace
        return player.getIntelligence() > 7; // Exemple simple
    }

    public boolean checkMagmaDogFight(Player player) {
        // Implémentation de la logique du combat contre le chien de magma
        // Retourne true si le joueur a gagné le combat
        return player.getAttack() > 10; // Exemple simple
    }

    public boolean checkCatalyzerFragment(Player player) {
        // Vérifie si le joueur a toutes les clés pour obtenir le fragment
        return player.hasItem(new Item("Clé Magmatique 1", "", ItemType.KEY)) &&
               player.hasItem(new Item("Clé Magmatique 2", "", ItemType.KEY)) &&
               player.hasItem(new Item("Clé Magmatique 3", "", ItemType.KEY));
    }
} 