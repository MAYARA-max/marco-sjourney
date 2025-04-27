package com.marcosjourney.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;

import com.marcosjourney.model.Enemy;
import com.marcosjourney.model.FrostiaPlanet;
import com.marcosjourney.model.GameMap;
import com.marcosjourney.model.GameState;
import com.marcosjourney.model.IgnisPlanet;
import com.marcosjourney.model.Item;
import com.marcosjourney.model.ItemType;
import com.marcosjourney.model.Planet;
import com.marcosjourney.model.Player;
import com.marcosjourney.model.Puzzle;
import com.marcosjourney.model.Quest;
import com.marcosjourney.model.Sortie;
import com.marcosjourney.model.Zone;
import com.marcosjourney.model.ZoneEffect;
import com.marcosjourney.ui.GameUI;
import com.marcosjourney.utils.SaveManager;

public class Game {
    private static final String SAVE_DIRECTORY = "saves";
    private Player player;
    private GameState gameState;
    private SaveManager saveManager;
    private Stack<Zone> zoneHistory;
    private Planet currentPlanet;
    private GameMap gameMap;
    private Quest catalystQuest;
    private boolean isRunning;
    private boolean isPaused;
    private Zone currentZone;
    private JFrame frame;
    private GameOutputCallback outputCallback;
    private boolean waitingForAction;
    private String currentAction;
    private Puzzle currentPuzzle;
    private Random random;
    private List<Planet> planets;

    public interface GameOutputCallback {
        void appendToOutput(String text);
    }

    public Game() {
        this.player = new Player("Marco", 100);
        this.saveManager = new SaveManager();
        this.zoneHistory = new Stack<>();
        this.gameMap = new GameMap();
        this.isRunning = false;
        this.isPaused = false;
        initializeCatalystQuest();
        this.random = new Random();
        this.planets = new ArrayList<>();
        initializePlanets();
    }

    private void initializeCatalystQuest() {
        Item catalystFragment = new Item("Fragment du Catalyseur", "Un fragment d'un artefact ancien", ItemType.ARTIFACT);
        this.catalystQuest = new Quest("La quête du Catalyseur", 
            "Retrouvez les fragments du Catalyseur dispersés sur différentes planètes", catalystFragment);
    }

    private void initializePlanets() {
        planets.add(new IgnisPlanet());
        planets.add(new FrostiaPlanet());
    }

    public void start() {
        isRunning = true;
        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        appendToOutput("Bienvenue dans Marco's Journey!");
        appendToOutput("Votre mission : retrouver les fragments du Catalyseur.");
    }

    public Player getPlayer() {
        return player;
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone zone) {
        this.currentZone = zone;
        if (player != null) {
            player.setCurrentZone(zone);
        }
    }

    public Zone getZoneByName(String zoneName) {
        if (currentPlanet != null) {
            return currentPlanet.getZoneByName(zoneName);
        }
        return null;
    }

    public void travelToIgnis() {
        for (Planet planet : planets) {
            if (planet instanceof IgnisPlanet) {
                currentPlanet = planet;
                setCurrentZone(planet.getStartingZone());
                break;
            }
        }
    }

    public void travelToFrostia() {
        for (Planet planet : planets) {
            if (planet instanceof FrostiaPlanet) {
                currentPlanet = planet;
                setCurrentZone(planet.getStartingZone());
                    break;
            }
        }
    }

    private void updatePlayerImage(String imageName) {
        if (outputCallback instanceof GameUI) {
            ((GameUI) outputCallback).updateMarcoImage(imageName);
        }
    }

    private void handleIgnisInteractions(Player player, Zone currentZone) {
        if (currentZone.getName().equals("Plateau Rocheux")) {
            handleFallingRock(player);
        } else if (currentZone.getName().equals("Vallée de Lave")) {
            handleLavaValley(player);
        } else if (currentZone.getName().equals("Caverne de Magma")) {
            handleMagmaCave(player);
        }
    }

    private void handleFallingRock(Player player) {
        if (Math.random() < 0.3) {
            appendToOutput("\nBip Bop : 'Attention ! Un rocher tombe du ciel !'");
            appendToOutput("Pour l'esquiver, tapez 'esquiver' suivi d'une direction (gauche, droite, avant, arrière)");
            waitingForAction = true;
            currentAction = "esquiver";
            currentPuzzle = Puzzle.createRandomPuzzle();
            appendToOutput("\nBip Bop : 'Mais avant, résolvez cette énigme : " + currentPuzzle.getQuestion() + "'");
        }
    }

    private void handleLavaValley(Player player) {
        if (Math.random() < 0.4) {
            appendToOutput("\nBip Bop : 'Une coulée de lave approche rapidement !'");
            appendToOutput("Pour trouver un abri, tapez 'chercher' suivi d'une direction (gauche, droite, avant, arrière)");
            waitingForAction = true;
            currentAction = "chercher";
            currentPuzzle = Puzzle.createRandomPuzzle();
            appendToOutput("\nBip Bop : 'Mais avant, résolvez cette énigme : " + currentPuzzle.getQuestion() + "'");
        }
    }

    private void handleMagmaCave(Player player) {
        if (!player.hasItem(new Item("Fragment du Catalyseur", "", ItemType.ARTIFACT))) {
            appendToOutput("\nBip Bop : 'Un Lava Demon garde l'entrée de la caverne !'");
            appendToOutput("Pour le combattre, tapez 'combattre' suivi du nom de votre arme");
            waitingForAction = true;
            currentAction = "combattre";
            currentPuzzle = Puzzle.createRandomPuzzle();
            appendToOutput("\nBip Bop : 'Mais avant, résolvez cette énigme : " + currentPuzzle.getQuestion() + "'");
        }
    }

    public void processAction(String command) {
        if (outputCallback != null) {
            outputCallback.appendToOutput("Commande reçue : " + command);
        }
    }

    private void move(Sortie direction) {
        Zone nextZone = currentZone.getExit(direction);
        if (nextZone != null) {
            currentZone = nextZone;
            player.setCurrentZone(nextZone);
            appendToOutput("\nVous allez vers " + direction.getDirection() + ".\n" + nextZone.getDescription());
            
            // Appliquer l'effet de la nouvelle zone
            if (currentZone.getEffect() != ZoneEffect.NONE) {
                currentZone.applyZoneEffect(player);
                appendToOutput("\nAttention ! Cette zone a un effet " + currentZone.getEffect().getDescription() + 
                    ".\nVotre santé : " + player.getHealth() + "/" + player.getMaxHealth());
            }
        } else {
            appendToOutput("\nVous ne pouvez pas aller dans cette direction.");
        }
    }

    private void combat(String enemyName) {
        Enemy enemy = currentZone.getEnemy(enemyName);
        if (enemy != null && enemy.isAlive()) {
            boolean victory = currentZone.combat(player);
            if (victory) {
                appendToOutput("Vous avez vaincu " + enemy.getName() + " !");
            } else if (player.getHealth() <= 0) {
                appendToOutput("Vous avez été vaincu...");
            } else {
                appendToOutput("Le combat continue ! Votre santé : " + player.getHealth() + 
                    "/" + player.getMaxHealth() + "\nSanté de " + enemy.getName() + " : " + 
                    enemy.getHealth());
            }
        } else {
            appendToOutput("Il n'y a pas d'ennemi de ce nom ici.");
        }
    }

    private void search() {
        if (currentZone.hasBeenSearched()) {
            appendToOutput("Vous avez déjà fouillé cette zone.");
            return;
    }

        if (!currentZone.getEnemies().isEmpty()) {
            appendToOutput("Impossible de fouiller la zone tant qu'il y a des ennemis !");
            return;
        }

        // Générer un puzzle aléatoire si nécessaire
        if (currentPuzzle == null) {
            currentPuzzle = Puzzle.createRandomPuzzle();
            appendToOutput("\nPour fouiller la zone, résolvez cette énigme :\n" + 
                currentPuzzle.getQuestion());
            return;
        }

        currentZone.search(player);
        appendToOutput("Vous avez fouillé la zone.");
    }

    private void showInventory() {
        StringBuilder sb = new StringBuilder("\n=== Inventaire ===\n");
        for (Item item : player.getInventory()) {
            sb.append("- ").append(item.getName()).append(" : ").append(item.getDescription()).append("\n");
        }
        appendToOutput(sb.toString());
    }

    private void showHelp() {
        appendToOutput("\nCommandes disponibles :");
        appendToOutput("- aller <direction> : se déplacer dans une direction");
        appendToOutput("- examiner : examiner la zone actuelle");
        appendToOutput("- inventaire : voir votre inventaire");
        appendToOutput("- aide : voir cette liste");
        appendToOutput("- quitter : quitter le jeu");
    }

    private void quit() {
        appendToOutput("\nAu revoir !");
        isRunning = false;
    }

    public void saveGame() {
        try {
            GameState state = new GameState(player, currentZone, currentPlanet, player.getInventory(), 0);
            saveManager.saveGame(state);
            appendToOutput("\nPartie sauvegardée avec succès !");
        } catch (Exception e) {
            appendToOutput("\nErreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public void loadGame() {
        try {
            GameState state = saveManager.loadGame();
            this.player = state.getPlayer();
            this.currentZone = state.getCurrentZone();
            this.currentPlanet = state.getCurrentPlanet();
            appendToOutput("\nPartie chargée avec succès !");
        } catch (Exception e) {
            appendToOutput("\nErreur lors du chargement : " + e.getMessage());
        }
    }

    private void appendToOutput(String text) {
        if (outputCallback != null) {
            outputCallback.appendToOutput(text);
        }
    }

    public void setOutputCallback(GameOutputCallback callback) {
        this.outputCallback = callback;
    }

    public void processCommand(String command) {
        if (!command.isEmpty()) {
            processAction(command);
        }
    }
}