package com.marcosjourney.model;

public class StationSpatiale extends Planet {
    
    public StationSpatiale() {
        super("Station Spatiale", "Une station spatiale abandonnée en orbite", Element.NONE);
    }
    
    @Override
    public void initializeZones() {
        // Créer les zones de la station spatiale
        Zone dockingBay = new Zone("Baie d'Amarrage", "Une grande baie où les vaisseaux peuvent s'amarrer.", "station_zone1.png");
        Zone controlRoom = new Zone("Salle de Contrôle", "Le centre de commande de la station.", "station_zone2.jpg");
        Zone livingQuarters = new Zone("Quartiers d'Habitation", "Les quartiers où vivait l'équipage.", "station_zone3.jpg");
        
        // Ajouter des ennemis
        Enemy malfunctioningRobot = new Enemy("Robot Défectueux", 35, 15, "Un robot de maintenance qui a mal tourné. Ses circuits endommagés le rendent imprévisible.");
        Enemy securitySystem = new Enemy("Système de Sécurité", 30, 20, "Le système de sécurité automatisé de la station, devenu hostile.");
        
        controlRoom.addEnemy(securitySystem);
        livingQuarters.addEnemy(malfunctioningRobot);
        
        // Configurer les connexions entre les zones
        dockingBay.addExit(Sortie.NORD, controlRoom);
        controlRoom.addExit(Sortie.SUD, dockingBay);
        controlRoom.addExit(Sortie.EST, livingQuarters);
        livingQuarters.addExit(Sortie.OUEST, controlRoom);
        
        // Ajouter les zones à la planète
        addZone(dockingBay);
        addZone(controlRoom);
        addZone(livingQuarters);
        
        // Définir la zone de départ
        setStartingZone(dockingBay);
    }
} 