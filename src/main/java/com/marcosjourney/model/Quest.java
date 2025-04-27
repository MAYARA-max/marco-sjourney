package com.marcosjourney.model;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private String name;
    private String description;
    private List<Item> requiredItems;
    private Item reward;
    private boolean isCompleted;

    public Quest(String name, String description, Item reward) {
        this.name = name;
        this.description = description;
        this.requiredItems = new ArrayList<>();
        this.reward = reward;
        this.isCompleted = false;
    }

    public void addRequiredItem(Item item) {
        requiredItems.add(item);
    }

    public boolean checkCompletion(Player player) {
        if (isCompleted) return true;
        
        for (Item requiredItem : requiredItems) {
            if (!player.hasItem(requiredItem)) {
                return false;
            }
        }
        
        isCompleted = true;
        if (reward != null) {
            player.addItem(reward);
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getRequiredItems() {
        return requiredItems;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Quête : ").append(name).append("\n");
        status.append("Description : ").append(description).append("\n");
        status.append("Progression : ");
        
        if (isCompleted) {
            status.append("Terminée !");
        } else {
            status.append(requiredItems.size()).append(" fragments à trouver");
        }
        
        return status.toString();
    }
} 