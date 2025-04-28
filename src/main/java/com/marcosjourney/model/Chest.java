package com.marcosjourney.model;

import java.util.ArrayList;
import java.util.List;

import com.marcosjourney.model.items.Item;

public class Chest {
    private List<Item> items;
    private boolean isLocked;
    private boolean isOpen;
    private String description;
    private Position position;

    public Chest(String description, Position position) {
        this.items = new ArrayList<>();
        this.isLocked = false;
        this.isOpen = false;
        this.description = description;
        this.position = position;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> open() {
        if (isLocked) {
            return null;
        }
        isOpen = true;
        List<Item> content = new ArrayList<>(items);
        items.clear();
        return content;
    }

    public void lock() {
        isLocked = true;
    }

    public void unlock() {
        isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getDescription() {
        return description;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
} 