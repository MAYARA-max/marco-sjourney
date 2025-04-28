package com.marcosjourney.model.npc;

import com.marcosjourney.model.quest.Puzzle;
import com.marcosjourney.model.quest.RandomPuzzle;

public class PNJ {
    private String name;
    private String imagePath;
    private String dialogue;
    private double positionX;
    private double positionY;
    private boolean hasInteracted;
    private Puzzle puzzle;

    public PNJ(String name, String imagePath, String dialogue, double positionX, double positionY) {
        this.name = name;
        this.imagePath = imagePath;
        this.dialogue = dialogue;
        this.positionX = positionX;
        this.positionY = positionY;
        this.hasInteracted = false;
        this.puzzle = RandomPuzzle.getRandomPuzzle();
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDialogue() {
        return dialogue;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public boolean hasInteracted() {
        return hasInteracted;
    }

    public void setHasInteracted(boolean hasInteracted) {
        this.hasInteracted = hasInteracted;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public boolean isNearPlayer(double playerX, double playerY) {
        // Vérifie si le joueur est à moins de 50 pixels du PNJ
        return Math.abs(playerX - positionX) < 50 && Math.abs(playerY - positionY) < 50;
    }
} 