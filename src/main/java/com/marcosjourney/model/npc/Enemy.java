package com.marcosjourney.model.npc;

import java.io.Serializable;

public class Enemy implements Serializable {
    private String name;
    private int health;
    private int maxHealth;
    private int attack;
    private String description;
    private boolean isAlive;

    public Enemy(String name, int maxHealth, int attack, String description) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attack = attack;
        this.description = description;
        this.isAlive = true;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        if (health == 0) {
            isAlive = false;
        }
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
        if (health > 0) {
            isAlive = true;
        }
    }

    public String getStatus() {
        return name + " - PV: " + health + " - Attaque: " + attack;
    }
} 
