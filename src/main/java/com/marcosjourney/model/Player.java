package com.marcosjourney.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health;
    private int maxHealth;
    private List<Item> inventory;
    private int attack;
    private int intelligence;
    private Item armor;
    private List<Item> equippedItems;
    private Zone currentZone;

    public Player(String name, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.inventory = new ArrayList<>();
        this.equippedItems = new ArrayList<>();
        this.attack = 10;
        this.intelligence = 10;
        this.armor = null;
        this.currentZone = null;
        
        // Ajouter les objets de base
        this.inventory.add(new Item("Potion magique", "Une potion qui restaure 20 points de vie", ItemType.HEALING, 20));
        this.inventory.add(new Item("Combinaison glaciale", "Protège contre les effets de froid", ItemType.PROTECTION, 0));
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public boolean hasItem(Item item) {
        return inventory.contains(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
    }

    public void takeDamage(int damage) {
        if (armor != null) {
            damage = armor.reduceDamage(damage);
        }
        health = Math.max(0, health - damage);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public Item getArmor() {
        return armor;
    }

    public void setArmor(Item armor) {
        if (armor != null && armor.getType() == ItemType.WEAPON) {
            this.armor = armor;
        }
    }

    public String getName() {
        return name;
    }

    public void showInventory() {
        System.out.println("\n=== Inventaire de " + name + " ===");
        if (inventory.isEmpty()) {
            System.out.println("L'inventaire est vide.");
        } else {
            for (Item item : inventory) {
                System.out.println("- " + item.getName() + " : " + item.getDescription());
            }
        }
        System.out.println("\nArmure équipée : " + (armor != null ? armor.getName() : "Aucune"));
    }

    public void increaseIntelligence() {
        this.intelligence++;
    }

    public void addQuest(Quest quest) {
        // TODO: Implement quest system
    }

    public void equipItem(Item item) {
        if (item.getType() == ItemType.PROTECTION) {
            // Déséquiper l'ancien équipement de protection s'il existe
            if (armor != null) {
                equippedItems.remove(armor);
            }
            armor = item;
            equippedItems.add(item);
        }
    }

    public void useItem(Item item) {
        if (item.getType() == ItemType.HEALING) {
            heal(item.getValue());
            inventory.remove(item);
        }
    }

    public List<Item> getEquippedItems() {
        return equippedItems;
    }

    public boolean isProtectedFrom(ZoneEffect effect) {
        for (Item item : equippedItems) {
            if (item.getType() == ItemType.PROTECTION) {
                if (effect == ZoneEffect.COLD && item.getName().equals("Combinaison glaciale")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone zone) {
        this.currentZone = zone;
    }

    public void addToInventory(Item item) {
        addItem(item);
    }
} 