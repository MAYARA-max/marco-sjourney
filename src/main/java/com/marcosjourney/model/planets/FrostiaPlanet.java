package com.marcosjourney.model.planets;

import com.marcosjourney.model.Element;
import com.marcosjourney.model.map.Sortie;
import com.marcosjourney.model.map.Zone;
import com.marcosjourney.model.npc.Enemy;

public class FrostiaPlanet extends Planet {
    public FrostiaPlanet() {
        super("Frostia", "Une planète glaciale où règne un froid éternel", Element.WATER);
    }
    
    @Override
    public void initializeZones() {
        // Créer les zones de la planète glacée
        Zone startingZone = new Zone("Base Glaciale", "Une base scientifique construite sur la glace éternelle.", "frostia_zone1.png");
        Zone frozenLake = new Zone("Lac Gelé", "Un immense lac gelé où la glace craque sous vos pas.", "frostia_zone2.jpg");
        Zone iceCavern = new Zone("Caverne de Glace", "Une grotte aux parois de glace cristalline reflétant la lumière.", "frostia_zone3.jpg");
        
        // Ajouter des ennemis
        Enemy iceGolem = new Enemy("Golem de Glace", 60, 10, "Une imposante créature faite de glace éternelle. Ses mouvements sont lents mais ses coups sont dévastateurs. La glace qui forme son corps semble vivante et pulse d'une énergie mystérieuse.");
        Enemy frostSpirit = new Enemy("Esprit du Givre", 35, 25, "Une entité éthérée qui flotte dans l'air glacial. Son corps translucide émet une lueur bleutée et laisse une traînée de cristaux de glace dans son sillage. Ses attaques rapides peuvent geler instantanément ce qu'elles touchent.");
        
        frozenLake.addEnemy(iceGolem);
        iceCavern.addEnemy(frostSpirit);
        
        // Configurer les connexions entre les zones
        startingZone.addExit(Sortie.NORD, frozenLake);
        frozenLake.addExit(Sortie.SUD, startingZone);
        frozenLake.addExit(Sortie.EST, iceCavern);
        iceCavern.addExit(Sortie.OUEST, frozenLake);
        
        // Ajouter les zones à la planète
        addZone(startingZone);
        addZone(frozenLake);
        addZone(iceCavern);
        
        // Définir la zone de départ
        setStartingZone(startingZone);
    }
}