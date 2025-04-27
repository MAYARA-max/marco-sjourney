package com.marcosjourney.model;

public class Item {
    private String name;
    private String description;
    private ItemType type;
    private String imagePath;
    private int defense;
    private Element element;
    private int value;
    private int power;

    public Item(String name, String description, ItemType type) {
        this(name, description, type, 0);
    }

    public Item(String name, String description, ItemType type, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
        this.imagePath = "resources/images/items/" + name.toLowerCase().replace(" ", "_") + ".png";
        this.defense = type == ItemType.PROTECTION ? 5 : 0;
        this.power = type == ItemType.WEAPON ? value : 0;
        this.element = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getDefense() {
        return defense;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getValue() {
        return value;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void use(Player player) {
        if (type == ItemType.HEALING) {
            player.heal(value);
        }
    }

    public int reduceDamage(int damage) {
        if (type == ItemType.PROTECTION) {
            return Math.max(1, damage - defense);
        }
        return damage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name) && type == item.type;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + type.hashCode();
    }
}

