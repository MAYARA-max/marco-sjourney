package com.marcosjourney.model;

public class Armor {
    private String name;
    private int defense;
    private Element element;

    public Armor(String name, int defense, Element  element) {
        this.name = name;
        this.defense = defense;
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }

    public Element getElement() {
        return element;
    }

    public int reduceDamage(int damage) {
        return Math.max(1, damage - defense);
    }

    public void impregnate(Element element) {
        this.element = element;
        System.out.println("Votre combinaison a été imprégnée de l'élément " + element);
    }
} 