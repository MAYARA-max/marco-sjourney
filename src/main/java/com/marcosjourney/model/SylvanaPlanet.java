package com.marcosjourney.model;

public class SylvanaPlanet extends Planet {
    public SylvanaPlanet() {
        super("Sylvana", "Une planète luxuriante couverte de forêts mystérieuses", Element.EARTH);
    }

    @Override
    public void initializeZones() {
        // Créer les zones de la planète forestière
        Zone startingZone = new Zone("Clairière d'Atterrissage", "Une clairière paisible où votre vaisseau a atterri.", "sylvana_zone1.png");
        Zone mysteriousForest = new Zone("Forêt Mystérieuse", "Une forêt dense aux arbres millénaires.", "sylvana_zone2.jpg");
        Zone ancientTemple = new Zone("Temple Ancien", "Un temple envahi par la végétation.", "sylvana_zone3.jpg");
        
        // Ajouter des ennemis
        Enemy forestGuardian = new Enemy("Gardien de la Forêt", 40, 15, "Un esprit ancien qui protège la forêt. Son corps semble fait de branches et de feuilles entrelacées.");
        Enemy wildBeast = new Enemy("Bête Sauvage", 25, 20, "Une créature féroce qui rôde dans les bois. Ses yeux brillent d'une lueur verdâtre.");
        
        mysteriousForest.addEnemy(wildBeast);
        ancientTemple.addEnemy(forestGuardian);
        
        // Configurer les connexions entre les zones
        startingZone.addExit(Sortie.NORD, mysteriousForest);
        mysteriousForest.addExit(Sortie.SUD, startingZone);
        mysteriousForest.addExit(Sortie.EST, ancientTemple);
        ancientTemple.addExit(Sortie.OUEST, mysteriousForest);
        
        // Ajouter les zones à la planète
        addZone(startingZone);
        addZone(mysteriousForest);
        addZone(ancientTemple);
        
        // Définir la zone de départ
        setStartingZone(startingZone);
    }
} 