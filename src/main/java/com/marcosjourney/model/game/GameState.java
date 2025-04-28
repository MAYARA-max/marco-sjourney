package com.marcosjourney.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.marcosjourney.model.player.Player;
import com.marcosjourney.model.map.Zone;
import com.marcosjourney.model.planets.Planet;
import com.marcosjourney.model.items.Item;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Player player;
    private Zone currentZone;
    private Planet currentPlanet;
    private List<Item> inventory;
    private int score;
    private int catalystFragments;
    private boolean isGameOver;
    private boolean hasWon;

    public GameState(Player player, Zone currentZone, Planet currentPlanet, List<Item> inventory, int score) {
        this.player = player;
        this.currentZone = currentZone;
        this.currentPlanet = currentPlanet;
        this.inventory = new ArrayList<>(inventory);
        this.score = score;
        this.catalystFragments = 0;
        this.isGameOver = false;
        this.hasWon = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public int getScore() {
        return score;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    public void setCurrentPlanet(Planet currentPlanet) {
        this.currentPlanet = currentPlanet;
    }

    public void addCatalystFragment() {
        this.catalystFragments++;
        if (this.catalystFragments >= 3) {
            this.hasWon = true;
            this.isGameOver = true;
        }
    }

    public int getCatalystFragments() {
        return catalystFragments;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setScore(int score) {
        this.score = score;
    }
} 