package com.marcosjourney.model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.Serializable;
import com.marcosjourney.model.items.Item;
import com.marcosjourney.model.npc.Enemy;
import com.marcosjourney.model.quest.Puzzle;
import com.marcosjourney.model.player.Player;
import com.marcosjourney.model.npc.PNJ;

public class Zone implements Serializable {
    private String name;
    private String description;
    private String imagePath;
    private Map<Sortie, Zone> exits;
    private List<Item> items;
    private List<Enemy> enemies;
    private boolean isLocked;
    private Item requiredKey;
    private Enemy enemy;
    private Puzzle puzzle;
    private Map<Sortie, Zone> sorties;
    private ZoneEffect effect;
    private List<Item> hiddenItems;
    private boolean hasBeenSearched;
    private List<PNJ> pnjs;
    private static final Random random = new Random();

    public Zone(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.isLocked = false;
        this.requiredKey = null;
        this.enemy = null;
        this.puzzle = null;
        this.sorties = new HashMap<>();
        this.effect = ZoneEffect.NONE;
        this.hiddenItems = new ArrayList<>();
        this.hasBeenSearched = false;
        this.pnjs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageFileName() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void addExit(Sortie direction, Zone destination) {
        exits.put(direction, destination);
    }

    public Zone getExit(Sortie direction) {
        return exits.get(direction);
    }

    public Map<Sortie, Zone> getExits() {
        return new HashMap<>(exits);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        this.enemy = enemy;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        if (this.enemy == enemy) {
            this.enemy = null;
        }
    }

    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public boolean hasEnemy(String enemyName) {
        return enemies.stream()
                     .anyMatch(e -> e.getName().equalsIgnoreCase(enemyName) && e.isAlive());
    }

    public Enemy getEnemy(String enemyName) {
        return enemies.stream()
                     .filter(e -> e.getName().equalsIgnoreCase(enemyName) && e.isAlive())
                     .findFirst()
                     .orElse(null);
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setRequiredKey(Item key) {
        this.requiredKey = key;
    }

    public Item getRequiredKey() {
        return requiredKey;
    }

    public String getFullDescription() {
        StringBuilder description = new StringBuilder(this.description + "\n");
        
        if (!items.isEmpty()) {
            description.append("\nObjets présents:\n");
            for (Item item : items) {
                description.append("- ").append(item.getName()).append("\n");
            }
        }
        
        if (!enemies.isEmpty()) {
            description.append("\nEnnemis présents:\n");
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    description.append("- ").append(enemy.getName()).append("\n");
                }
            }
        }
        
        description.append("\nSorties disponibles:\n");
        for (Map.Entry<Sortie, Zone> exit : exits.entrySet()) {
            description.append("- ").append(exit.getKey().getDirection())
                      .append(" vers ").append(exit.getValue().getName()).append("\n");
        }
        
        return description.toString();
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void generateRandomPuzzle() {
        this.puzzle = Puzzle.createRandomPuzzle();
    }

    public void lock(Item key) {
        this.isLocked = true;
        this.requiredKey = key;
    }

    public boolean unlock(Item key) {
        if (key == this.requiredKey) {
            this.isLocked = false;
            return true;
        }
        return false;
    }

    public Map<String, Zone> getExitsAsMap() {
        Map<String, Zone> map = new HashMap<>();
        for (Map.Entry<Sortie, Zone> entry : exits.entrySet()) {
            map.put(entry.getKey().getDirection(), entry.getValue());
        }
        return map;
    }

    public void setEffect(ZoneEffect effect) {
        this.effect = effect;
    }

    public ZoneEffect getEffect() {
        return effect;
    }

    public void addHiddenItem(Item item) {
        hiddenItems.add(item);
    }

    public void search(Player player) {
        if (enemy != null && enemy.isAlive()) {
            System.out.println("Impossible de fouiller la zone tant que " + enemy.getName() + " est présent !");
            return;
        }
        
        for (Item item : hiddenItems) {
            player.addItem(item);
            System.out.println("Vous avez trouvé : " + item.getName());
        }
        hiddenItems.clear();
        hasBeenSearched = true;
    }

    public boolean combat(Player player) {
        if (enemy == null || !enemy.isAlive()) {
            return true;
        }

        // Le joueur attaque
        enemy.takeDamage(player.getAttack());

        // L'ennemi riposte s'il est encore en vie
        if (enemy.isAlive()) {
            player.takeDamage(enemy.getAttack());
        }

        return !enemy.isAlive();
    }

    public boolean solveSearchPuzzle(String answer) {
        if (puzzle != null && !hasBeenSearched) {
            if (puzzle.checkAnswer(answer)) {
                System.out.println(puzzle.getSuccessMessage());
                hasBeenSearched = true;
                if (!hiddenItems.isEmpty()) {
                    System.out.println("\nVous avez trouvé des objets cachés !");
                    for (Item item : hiddenItems) {
                        System.out.println("- " + item.getName());
                    }
                }
                return true;
            } else {
                System.out.println(puzzle.getFailureMessage());
            }
        }
        return false;
    }

    public void applyZoneEffect(Player player) {
        if (effect != null && effect != ZoneEffect.NONE) {
            switch (effect) {
                case POISON:
                    player.takeDamage(5);
                    break;
                case HEALING:
                    player.heal(10);
                    break;
                case DAMAGE:
                    player.takeDamage(10);
                    break;
                case COLD:
                    if (!player.isProtectedFrom(ZoneEffect.COLD)) {
                        player.takeDamage(15);
                    }
                    break;
            }
        }
    }

    public void describe() {
        System.out.println("\n=== " + name + " ===");
        System.out.println(description);
        
        if (!items.isEmpty()) {
            System.out.println("\nObjets présents :");
            for (Item item : items) {
                System.out.println("- " + item.getName());
            }
        }

        System.out.println("\nSorties disponibles :");
        for (String direction : getExitsAsMap().keySet()) {
            System.out.println("- " + direction);
        }

        if (enemy != null) {
            System.out.println("\nAttention ! " + enemy.getName() + " est présent dans cette zone !");
        }

        if (effect != ZoneEffect.NONE) {
            System.out.println("\nEffet de zone : " + effect.getDescription());
        }
    }

    public String descriptionLongue() {
        StringBuilder sb = new StringBuilder(description);
        sb.append("\n\nSorties disponibles :");
        for (String direction : getExitsAsMap().keySet()) {
            sb.append("\n- ").append(direction);
        }
        if (enemy != null && enemy.isAlive()) {
            sb.append("\n\nAttention ! ").append(enemy.getName()).append(" est présent dans cette zone !");
        }
        return sb.toString();
    }

    public List<Item> getHiddenItems() {
        return new ArrayList<>(hiddenItems);
    }

    public void clearHiddenItems() {
        hiddenItems.clear();
    }

    public boolean hasBeenSearched() {
        return hasBeenSearched;
    }

    public void setHasBeenSearched(boolean searched) {
        this.hasBeenSearched = searched;
    }

    public void addPNJ(PNJ pnj) {
        pnjs.add(pnj);
    }

    public List<PNJ> getPNJs() {
        return new ArrayList<>(pnjs);
    }

    public PNJ getPNJByName(String name) {
        return pnjs.stream()
                  .filter(p -> p.getName().equalsIgnoreCase(name))
                  .findFirst()
                  .orElse(null);
    }

    public boolean isPlayerNearPNJ(double playerX, double playerY) {
        return pnjs.stream().anyMatch(pnj -> pnj.isNearPlayer(playerX, playerY));
    }

    public PNJ getNearestPNJ(double playerX, double playerY) {
        return pnjs.stream()
                  .filter(pnj -> pnj.isNearPlayer(playerX, playerY))
                  .findFirst()
                  .orElse(null);
    }
}

