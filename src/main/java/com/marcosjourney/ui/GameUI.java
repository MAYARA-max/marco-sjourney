package com.marcosjourney.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.marcosjourney.game.Game;
import com.marcosjourney.model.game.GameState;
import com.marcosjourney.model.items.Item;
import com.marcosjourney.model.items.ItemType;
import com.marcosjourney.model.map.Sortie;
import com.marcosjourney.model.map.Zone;
import com.marcosjourney.model.map.ZoneEffect;
import com.marcosjourney.model.npc.PNJ;
import com.marcosjourney.utils.SaveManager;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameUI extends Application implements Game.GameOutputCallback {
    private Game game;
    private TextArea outputArea;
    private ImageView zoneImageView;
    private VBox commandsBox;
    private SaveManager saveManager;
    private Label descriptionLabel;
    private ImageView marcoImageView;
    private ImageView planetImageView;
    private ImageView enemyImageView;
    private boolean isMarcoMoving = false;
    private double marcoStartX;
    private double marcoEndX;
    private Timeline marcoAnimation;
    private String selectedPlanet = null;
    private boolean waitingForEnigmaAnswer = false;
    private boolean waitingForRiddleAnswer = false;
    private boolean awaitingItemSelection = false;
    private String itemAction = null;
    private GridPane inventoryGrid;
    private VBox buttonBox;
    private TextField commandInput;
    private Label welcomeLabel;
    private Stage primaryStage;
    private BorderPane root;
    private String currentRiddle;
    private Zone currentZone;
    private boolean waitingForDodge = false;
    private boolean hasIceSpearEquipped = false;
    private StackPane gameView;
    private boolean waitingForPlanetSelection = true;
    private int playerLives = 5;
    private boolean isInRockDodgeSequence = false;
    private int currentRockIndex = 0;
    private String[] correctRockDodgeSequence = {"droite", "droite", "gauche"};
    private List<String> playerRockDodgeAttempts = new ArrayList<>();
    private boolean waitingForZoneSelection = false;
    private boolean waitingForPuzzleAnswer = false;
    private PNJ currentPNJ = null;
    private static final int MOVEMENT_DISTANCE = 30;
    private int rockAttempts = 0;
    private ImageView dragonImageView;  // Nouvelle variable de classe
    private ImageView BipBopImageView;  
    private boolean waitingForCombatChoice = false;
    private boolean waitingForCrystalPuzzle = false;
    private boolean waitingForCrystalAnswer = false;
    private boolean waitingForFrostiaZoneChoice = false;
    private boolean waitingForBoxSelection = false;
    private boolean waitingForBoxPuzzle = false;
    private int boxesSolved = 0;
    private int boxesAttempted = 0;
    private int currentPuzzleIndex = 0;
    private boolean waitingForIceCavern = false;
    private boolean waitingForPotion = false;
    private boolean waitingForSylvanaEquation = false;
    private boolean waitingForSylvanaZone = false;
    private boolean waitingForMaze = false;
    private String[] mazeSolution = null;
    private int mazeStep = 0;
    private boolean waitingForBipBopTalk = false;
    private boolean waitingForHangman = false;
    private StringBuilder hangmanWord = null;
    private String hangmanTarget = "paix";
    private int hangmanTries = 6;
    private boolean[] hangmanRevealed = null;
    private String[] puzzles = {
        "Je suis liquide mais je peux être solide, qui suis-je ? (réponse : eau)",
        "J'ai des branches mais pas de feuilles, qui suis-je ? (réponse : rivière)",
        "Je tombe sans jamais me faire mal, qui suis-je ? (réponse : neige)",
        "Je suis froid et transparent, qui suis-je ? (réponse : glace)"
    };
    private String[] answers = {"eau", "rivière", "neige", "glace"};
    private int[][] boxPositions = null;
    private boolean waitingForLabyrintheCommand = false;

    // Ajouter une variable pour gérer la pause
    private boolean isPaused = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        game = new Game();
        game.setOutputCallback(this);
        saveManager = new SaveManager();
        
        // Initialiser l'inventaire avec les objets de base
        game.getPlayer().addToInventory(new Item("Potion magique", "Une potion qui restaure 20 points de vie", ItemType.HEALING, 20));
        game.getPlayer().addToInventory(new Item("Potion magique", "Une potion qui restaure 20 points de vie", ItemType.HEALING, 20));
        game.getPlayer().addToInventory(new Item("Combinaison ignifugée", "Protège contre les effets de chaleur", ItemType.PROTECTION, 0));
        game.getPlayer().addToInventory(new Item("IceSpear", "Une lance de glace puissante contre les créatures de feu", ItemType.WEAPON, 0));
        
        initializeUI();
        
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Marco's Journey");
        primaryStage.setScene(scene);
        
        game.start();
        showWelcomeMessage();
        updateInventoryDisplay();
        
        primaryStage.show();
        commandInput.requestFocus();
    }

    private void initializeUI() {
        root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Créer la barre de menu supérieure
        HBox topMenuBar = new HBox(10);
        topMenuBar.setPadding(new Insets(5));
        topMenuBar.setStyle("-fx-background-color: #2c3e50;");
        topMenuBar.setAlignment(Pos.CENTER_RIGHT);

        // Créer les boutons
        Button menuButton = createStyledButton("Menu", 80);
        Button saveButton = createStyledButton("Sauvegarder", 100);
        Button pauseButton = createStyledButton("Pause", 80);

        // Ajouter les actions aux boutons
        menuButton.setOnAction(e -> showMenu());
        saveButton.setOnAction(e -> saveGame());
        pauseButton.setOnAction(e -> togglePause());

        // Ajouter les boutons à la barre de menu
        topMenuBar.getChildren().addAll(menuButton, saveButton, pauseButton);
        
        // Ajouter la barre de menu au BorderPane
        root.setTop(topMenuBar);
        
        // Initialize all UI components
        initializeLeftPanel();
        initializeCenterPanel();
        initializeRightPanel();
        initializeBottomPanel();
        
        setupButtons();
    }

    private Button createStyledButton(String text, double width) {
        Button button = new Button(text);
        button.setMinWidth(width);
        button.setStyle(
            "-fx-background-color: #4a90e2;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 16px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        // Effet de survol
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #357abd;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 16px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #4a90e2;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 16px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        ));
        
        return button;
    }

    private Button createDirectionButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(70);
        button.setStyle(
            "-fx-background-color: #2ecc71;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 6px 12px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        // Effet de survol
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 6px 12px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #2ecc71;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 6px 12px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        ));
        
        return button;
    }
    
    private void showWelcomeMessage() {
        StringBuilder welcome = new StringBuilder();
        welcome.append("=== Bienvenue dans Marco's Journey! ===\n\n");
        welcome.append("Cher Marco,\n\n");
        welcome.append("Tu as été choisi comme dernier héraut de l'humanité pour une mission cruciale.\n");
        welcome.append("Notre planète Terre est à bout de souffle. Les ressources énergétiques sont épuisées,\n");
        welcome.append("et plus de 20 milliards d'êtres humains luttent pour leur survie quotidienne.\n");
        welcome.append("La famine et les conflits ravagent notre monde.\n\n");
        welcome.append("Ta mission : trouver un artéfact extra-terrestre précieux qui pourrait\n");
        welcome.append("permettre à l'humanité de retrouver sa splendeur d'antan.\n\n");
        welcome.append("Bip Bop : 'Salut Marco ! Je suis Bip Bop, ton assistant IA.\n");
        welcome.append("Pour commencer ta mission, nous devons nous rendre sur Ignis, une planète volcanique aux températures extrêmes.\n");
        welcome.append("Utilise la commande 'voyager' pour accéder au panneau de contrôle de la station spatiale.\n");
        welcome.append("Mais attention ! La planète est protégée par un système de sécurité.\n");
        welcome.append("Tu devras faire preuve d'intelligence pour y accéder.'\n\n");
        welcome.append("Que l'aventure commence !\n");
        
        appendToOutput(welcome.toString());
    }

    @Override
    public void appendToOutput(String text) {
        if (outputArea != null) {
            System.out.println("Debug - Ajout au texte de sortie: " + text);
            outputArea.appendText(text + "\n");
            outputArea.setScrollTop(Double.MAX_VALUE);
        } else {
            System.out.println("Debug - outputArea est null!");
        }
    }
    
    private void updateDisplay() {
        currentZone = game.getCurrentZone();
        if (currentZone != null) {
            descriptionLabel.setText(currentZone.getDescription());
            try {
                String imagePath = currentZone.getImageFileName();
                Image zoneImage = new Image(getClass().getResourceAsStream(imagePath));
                zoneImageView.setImage(zoneImage);
                
                // Réinitialiser la position de Marco pour la nouvelle zone
                marcoImageView.setTranslateX(0);
                marcoImageView.setTranslateY(0);
                
                // Message de Bip Bop pour la nouvelle zone
                appendToOutput("\nBip Bop : 'Vous êtes maintenant dans " + currentZone.getName() + ".'");
                
                // Vérifier s'il y a des ennemis
                if (!currentZone.getEnemies().isEmpty()) {
                    appendToOutput("\nBip Bop : 'Attention ! Il y a des ennemis dans cette zone !'");
                    appendToOutput("Pour combattre, utilisez la commande 'combattre' suivi du nom de l'ennemi.");
                }
                
                // Suggérer d'examiner la zone
                appendToOutput("\nBip Bop : 'Je vous conseille d'examiner la zone avec la commande 'examiner'.");
                appendToOutput("Vous pouvez aussi chercher des objets avec la commande 'chercher'.");
                
                // Afficher les sorties disponibles
                appendToOutput("\nBip Bop : 'Les sorties disponibles sont :'");
                for (Map.Entry<Sortie, Zone> exit : currentZone.getExits().entrySet()) {
                    appendToOutput("- " + exit.getKey().getDirection() + " vers " + exit.getValue().getName());
                }
                appendToOutput("\nPour vous déplacer, utilisez la commande 'aller' suivie de la direction.");
                
                if (currentZone.getEffect() != ZoneEffect.NONE) {
                    currentZone.applyZoneEffect(game.getPlayer());
                    appendToOutput("\nBip Bop : 'Attention ! Cette zone a un effet " + 
                        currentZone.getEffect().getDescription() + 
                        ".\nVotre santé actuelle : " + game.getPlayer().getHealth() + 
                        "/" + game.getPlayer().getMaxHealth());
                }
            } catch (Exception e) {
                System.err.println("Erreur de chargement de l'image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showHelp() {
        appendToOutput("\n=== Aide ===");
        appendToOutput("Commandes de base :");
        appendToOutput("- 'menu' : Afficher le menu");
        appendToOutput("- 'aide' : Afficher cette aide");
        appendToOutput("- 'inventaire' : Gérer votre inventaire");
        appendToOutput("- 'examiner' : Examiner la zone actuelle");
        appendToOutput("- 'chercher' : Chercher des objets");
        appendToOutput("\nCommandes de déplacement :");
        appendToOutput("- 'haut' : Aller vers le haut");
        appendToOutput("- 'bas' : Aller vers le bas");
        appendToOutput("- 'gauche' : Aller vers la gauche");
        appendToOutput("- 'droite' : Aller vers la droite");
    }

    private void initializePNJ() {
        try {
            // Vérifier si l'image existe
            String imagePath = "/images/dragon.png";
            System.out.println("Debug - Tentative de chargement de l'image du dragon: " + imagePath);
            
            if (getClass().getResource(imagePath) == null) {
                System.err.println("Erreur - L'image du dragon n'existe pas: " + imagePath);
                return;
            }
            
            // Créer une ImageView pour le dragon
            Image dragonImage = new Image(getClass().getResourceAsStream(imagePath));
            if (dragonImage.isError()) {
                System.err.println("Erreur lors du chargement de l'image du dragon: " + dragonImage.getException());
                return;
            }
            
            ImageView dragonImageView = new ImageView(dragonImage);
            dragonImageView.setFitWidth(50);
            dragonImageView.setFitHeight(50);
            dragonImageView.setPreserveRatio(true);
            dragonImageView.setTranslateX(50);
            dragonImageView.setTranslateY(50);
            
            // Retirer l'ancienne image du dragon si elle existe
            gameView.getChildren().removeIf(node -> node instanceof ImageView && 
                ((ImageView) node).getImage().getUrl().contains("dragon.png"));
            
            // Ajouter l'image du dragon à la scène
            gameView.getChildren().add(dragonImageView);
            System.out.println("Debug - Image du dragon ajoutée à la scène");
            
            // Créer le PNJ dragon
            PNJ dragon = new PNJ("Dragon", imagePath, 
                "Pour trouver le premier fragment du Catalyseur, tu dois affronter le Lava Demon qui le protège dans la Caverne de Magma. " +
                "Le Lava Demon est une créature puissante faite de roche en fusion. " +
                "Mais avant d'y aller, résous cette énigme pour prouver ta valeur...", 150, 200);
                
            Zone lavaValley = game.getZoneByName("LavaValley");
            if (lavaValley != null) {
                lavaValley.addPNJ(dragon);
                System.out.println("Debug - PNJ ajouté à LavaValley avec succès");
            } else {
                System.out.println("Debug - Erreur: Zone LavaValley non trouvée");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du PNJ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void processCommand(String command) {
        System.out.println("Debug - Commande reçue : " + command);
        
        // Commandes de base
        if (command.equalsIgnoreCase("aide")) {
            showHelp();
            return;
        }
        
        if (command.equalsIgnoreCase("menu")) {
            System.out.println("Debug - Affichage du menu");
            showMenu();
            return;
        }
        
        if (command.equalsIgnoreCase("quitter")) {
            appendToOutput("\nAu revoir !");
            System.exit(0);
            return;
        }
        
        if (command.equalsIgnoreCase("sauvegarder")) {
            appendToOutput("\nSauvegarde de la partie...");
            return;
        }
        
        if (command.equalsIgnoreCase("pause")) {
            appendToOutput("\nJeu en pause ! Tapez 'reprendre' pour continuer.");
            return;
        }
        
        if (command.equalsIgnoreCase("reprendre")) {
            appendToOutput("\nJeu repris !");
            return;
        }

        // Gestion du Lava Demon uniquement dans ignis_zone3
        if (currentZone != null && currentZone.getName().equals("ignis_zone3")) {
            if (command.equalsIgnoreCase("combattre")) {
                startLavaDemonFight();
                return;
            }
        } else {
            // Nettoyer le Lava Demon si on n'est pas dans ignis_zone3
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("lava_demon"));
        }

        System.out.println("État actuel - waitingForZoneSelection: " + waitingForZoneSelection);
        System.out.println("Debug - État du labyrinthe au début :");
        System.out.println("Debug - waitingForLabyrintheCommand = " + waitingForLabyrintheCommand);
        System.out.println("Debug - waitingForMaze = " + waitingForMaze);
        System.out.println("Debug - mazeStep = " + mazeStep);
        System.out.println("Debug - mazeSolution = " + (mazeSolution != null ? Arrays.toString(mazeSolution) : "null"));

        if (waitingForHangman) {
            System.out.println("Debug - Traitement d'une lettre pour le pendu");
            processHangman(command);
            return;
        }

        if (awaitingItemSelection) {
            processItemSelection(command);
            return;
        }

        if (waitingForLabyrintheCommand && command.equalsIgnoreCase("labyrinthe")) {
            Image maze = new Image(getClass().getResourceAsStream("/images/maze.png"));
            zoneImageView.setImage(maze);
            appendToOutput("Utilise les commandes : avancer, reculer, haut, bas. Trouve le bon chemin en 3 mouvements !");
            mazeSolution = new String[]{"avancer", "avancer", "haut"};
            mazeStep = 0;
            waitingForMaze = true;
            waitingForLabyrintheCommand = false;
            appendToOutput("Debug - Labyrinthe initialisé : waitingForMaze = " + waitingForMaze + ", mazeStep = " + mazeStep);
            return;
        }

        if (waitingForMaze) {
            appendToOutput("Debug - État du labyrinthe :");
            appendToOutput("Debug - waitingForMaze = " + waitingForMaze);
            appendToOutput("Debug - mazeStep = " + mazeStep);
            appendToOutput("Debug - Commande attendue = " + mazeSolution[mazeStep]);
            appendToOutput("Debug - Commande reçue = " + command);
            
            if (command.equalsIgnoreCase(mazeSolution[mazeStep])) {
                mazeStep++;
                appendToOutput("Debug - Bonne commande ! Nouveau mazeStep = " + mazeStep);
                if (mazeStep == mazeSolution.length) {
                    appendToOutput("Bravo ! Tu as trouvé la sortie ! Tu peux aller à 'ancienttemple'.");
                    waitingForMaze = false;
                    waitingForSylvanaZone = true;
                } else {
                    appendToOutput("Continue !");
                }
            } else {
                appendToOutput("Tu t'es perdu dans la forêt ! Recommence.");
                mazeStep = 0;
                appendToOutput("Debug - Mauvaise commande ! mazeStep réinitialisé à 0");
            }
            return;
        }

        if (waitingForZoneSelection) {
            if (command.equals("1") || command.equals("2")) {
                try {
                    String imagePath = command.equals("1") ? "/images/ignis_zone2.jpg" : "/images/ignis_zone3.jpg";
                    System.out.println("Debug - Tentative de chargement de l'image de zone: " + imagePath);
                    
                    // Vérifier si le fichier existe
                    if (getClass().getResource(imagePath) == null) {
                        System.err.println("Erreur - L'image n'existe pas: " + imagePath);
                        appendToOutput("Erreur: Impossible de charger l'image de la zone. Veuillez réessayer.");
                        return;
                    }
                    
                    Image newZoneImage = new Image(getClass().getResourceAsStream(imagePath));
                    if (newZoneImage.isError()) {
                        System.err.println("Erreur lors du chargement de l'image: " + newZoneImage.getException());
                        appendToOutput("Erreur: L'image est corrompue. Veuillez réessayer.");
                        return;
                    }
                    
                    zoneImageView.setImage(newZoneImage);
                    
                    if (command.equals("1")) {
                        System.out.println("Debug - Tentative de chargement de l'image de Marco: marco_firysuit.png");
                        updateMarcoImage("marco_firysuit.png");
                        
                        // Ajouter le dragon dans LavaValley
                        try {
                            // Charger l'image du dragon
                            Image dragonImage = new Image(getClass().getResourceAsStream("/images/dragon.png"));
                            if (dragonImage.isError()) {
                                System.err.println("Debug - Erreur lors du chargement de l'image du dragon: " + dragonImage.getException());
                                return;
                            }
                            
                            // Créer ou mettre à jour l'ImageView du dragon
                            if (dragonImageView != null) {
                                gameView.getChildren().remove(dragonImageView);
                            }
                            
                            dragonImageView = new ImageView(dragonImage);
                            dragonImageView.setFitWidth(100);
                            dragonImageView.setFitHeight(100);
                            dragonImageView.setPreserveRatio(true);
                            dragonImageView.setTranslateX(70);
                            dragonImageView.setTranslateY(70);
                            
                            // S'assurer que le dragon est ajouté après l'image de fond mais avant Marco
                            gameView.getChildren().add(1, dragonImageView);
                            System.out.println("Debug - Dragon ajouté à la scène");
                            
                        } catch (Exception e) {
                            System.err.println("Erreur lors de l'ajout du dragon: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        // Retirer le dragon si on va dans une autre zone
                        if (dragonImageView != null) {
                            gameView.getChildren().remove(dragonImageView);
                        }
                    }
                    
                    marcoImageView.setTranslateX(0);
                    marcoImageView.setTranslateY(0);
                    
                    String zoneName = command.equals("1") ? "LavaValley" : "MagmaCave";
                    appendToOutput("\nBip Bop : 'Te voilà dans " + zoneName + " !'");
                    appendToOutput("Tu peux te déplacer avec les commandes suivantes :");
                    appendToOutput("- 'haut' : monter");
                    appendToOutput("- 'bas' : descendre");
                    appendToOutput("- 'avancer' : aller vers l'avant");
                    appendToOutput("- 'reculer' : aller vers l'arrière");
                    appendToOutput("- 'dragon' : aller vers le dragon");
                    
                    waitingForZoneSelection = false;
                } catch (Exception e) {
                    System.err.println("Erreur détaillée lors du chargement de l'image: " + e.getMessage());
                    e.printStackTrace();
                    appendToOutput("Erreur: Impossible de charger les images. Veuillez réessayer.");
                }
            } else {
                appendToOutput("Choix invalide. Veuillez entrer 1 pour LavaValley ou 2 pour MagmaCave.");
            }
            return;
        }

        if (command.equals("voyager")) {
            appendToOutput("\nBip Bop : 'Bienvenue dans l'aventure ! Pour commencer, nous allons explorer Ignis, une planète volcanique aux températures extrêmes.'");
            appendToOutput("Pour accéder au panneau de contrôle, utilisez la commande 'monter'.");
            return;
        }

        if (command.equals("monter")) {
            marcoImageView.setTranslateY(marcoImageView.getTranslateY() - 50);
            appendToOutput("\nBip Bop : 'Bien ! Maintenant que vous êtes devant le panneau de contrôle, résolvez cette énigme pour activer le portail vers Ignis :'");
            appendToOutput("\nJe suis chaud mais ne brûle pas toujours, je donne la vie mais peux aussi la reprendre. Qui suis-je ?");
            waitingForRiddleAnswer = true;
            return;
        }

        if (waitingForRiddleAnswer) {
            if (command.toLowerCase().contains("soleil")) {
                waitingForRiddleAnswer = false;
                try {
                    Image ignisImage = new Image(getClass().getResourceAsStream("/images/ignis_zone1.png"));
                    zoneImageView.setImage(ignisImage);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }

                appendToOutput("\nBip Bop : 'Bravo ! Le portail vers Ignis est activé. Vous êtes maintenant dans la Zone de Débarquement d'Ignis.'");
                appendToOutput("ATTENTION ! La chaleur est extrême ici. Vous perdez des points de vie !");
                game.getPlayer().takeDamage(20);
                appendToOutput("Points de vie actuels : " + game.getPlayer().getHealth() + "/" + game.getPlayer().getMaxHealth());
                appendToOutput("\nBip Bop : 'Vite ! Tapez la commande 'inventaire' pour accéder à votre équipement et protégez-vous de la chaleur avec votre combinaison ignifugée !'");
                
                // Initialiser le PNJ après avoir atteint Ignis
                initializePNJ();
            } else {
                appendToOutput("Ce n'est pas la bonne réponse. Réessayez !");
            }
            return;
        }

        // Gérer les commandes de déplacement
        if (command.equals("haut") || command.equals("bas") || 
            command.equals("avancer") || command.equals("reculer") ||
            command.equals("dragon")) {
            moveMarco(command);
            return;
        }

        if (command.equals("inventaire")) {
            showInventoryAndEquipItem();
            return;
        }

        if (isInRockDodgeSequence) {
            if (!command.equals("droite") && !command.equals("gauche")) {
                appendToOutput("Commande invalide ! Utilisez 'droite' ou 'gauche' pour éviter le rocher.");
                return;
            }

            rockAttempts++;
            playerRockDodgeAttempts.add(command);
            
            // Animer le déplacement de Marco
            double currentX = marcoImageView.getTranslateX();
            double deplacement = 50;
            
            if (command.equals("droite")) {
                marcoImageView.setTranslateX(currentX + deplacement);
            } else {
                marcoImageView.setTranslateX(currentX - deplacement);
            }

            if (command.equals(correctRockDodgeSequence[currentRockIndex])) {
                appendToOutput("\nBip Bop : 'Bien joué ! Vous avez évité le rocher !'");
            } else {
                playerLives--;
                appendToOutput("\nBip Bop : 'Aïe ! Le rocher vous a touché ! Il vous reste " + playerLives + " points de vie.'");
            }

            currentRockIndex++;

            if (rockAttempts >= 3) {
                // Vérifier le nombre de succès
                int successfulDodges = 0;
                for (int i = 0; i < correctRockDodgeSequence.length; i++) {
                    if (i < playerRockDodgeAttempts.size() && 
                        playerRockDodgeAttempts.get(i).equals(correctRockDodgeSequence[i])) {
                        successfulDodges++;
                    }
                }

                if (successfulDodges >= 2 && playerLives > 0) {
                    appendToOutput("\nBip Bop : 'Félicitations ! Vous avez réussi à traverser la zone des rochers !'");
                    appendToOutput("\nBip Bop : 'Maintenant, tu as accès aux autres zones ! Choisis ta destination :'");
                    appendToOutput("1_ LavaValley");
                    appendToOutput("2_ MagmaCave");
                    waitingForZoneSelection = true;
                    isInRockDodgeSequence = false;
                    playerRockDodgeAttempts.clear();
                    currentRockIndex = 0;
                    rockAttempts = 0;
                    System.out.println("En attente de sélection de zone..."); // Debug
                } else {
                    appendToOutput("\nBip Bop : 'Vous n'avez pas réussi à éviter assez de rochers. Il faut recommencer !'");
                    currentRockIndex = 0;
                    rockAttempts = 0;
                    playerRockDodgeAttempts.clear();
                    appendToOutput("\nPremier rocher en approche ! (droite/gauche) :");
                }
            } else {
                appendToOutput("\nBip Bop : 'Attention ! Rocher numéro " + (rockAttempts + 1) + " en approche ! (droite/gauche) :'");
            }
            return;
        }

        if (command.equals("parler")) {
            // Vérifier si Marco est devant le dragon (position 60,60)
            if (marcoImageView.getTranslateX() == 60 && marcoImageView.getTranslateY() == 60) {
                appendToOutput("\nDragon : 'Pour obtenir le fragment du Catalyseur, tu dois affronter le Lava Demon dans la Caverne de Magma.'");
                appendToOutput("\nBip Bop : 'Pour y aller, tape 'magmacave' pour accéder à la Caverne de Magma.'");
            } else {
                appendToOutput("\nBip Bop : 'Il n'y a personne à qui parler ici.'");
            }
            return;
        
        }

        if (command.equals("magmacave")) {
            try {
                // Charger l'image de la Caverne de Magma
                Image magmaCaveImage = new Image(getClass().getResourceAsStream("/images/ignis_zone3.jpg"));
                zoneImageView.setImage(magmaCaveImage);
                
                // Remplacer le dragon par le Lava Demon
                if (dragonImageView != null) {
                    gameView.getChildren().remove(dragonImageView);
                }
                
                // Charger l'image du Lava Demon
                Image lavaDemonImage = new Image(getClass().getResourceAsStream("/images/lava_demon.png"));
                dragonImageView = new ImageView(lavaDemonImage);
                dragonImageView.setFitWidth(100);
                dragonImageView.setFitHeight(100);
                dragonImageView.setPreserveRatio(true);
                dragonImageView.setTranslateX(70);
                dragonImageView.setTranslateY(70);
                
                // Ajouter le Lava Demon à la scène
                gameView.getChildren().add(1, dragonImageView);
                
                appendToOutput("\nBip Bop : 'Te voilà dans la Caverne de Magma !'");
                appendToOutput("Le Lava Demon est devant toi ! tape /affronter/ le chien de magma");
                
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des images: " + e.getMessage());
                appendToOutput("Erreur: Impossible de charger les images. Veuillez réessayer.");
            }
            return;
        }

        if (command.equals("affronter")) {
            appendToOutput("\nBip Bop : 'Le chien est devant toi ! Que veux-tu faire ?'");
            appendToOutput("Combattre");
            appendToOutput("Esquiver");
            waitingForCombatChoice = true;
            return;
        }

        if (waitingForCombatChoice) {
            if (command.equals("combattre")) {
                appendToOutput("\nBip Bop : 'Ouvre ton inventaire pour choisir l'IceSpear !'");
                showInventoryAndEquipItem();
                waitingForCombatChoice = false;
                
            } else if (command.equals("esquiver")) {
                appendToOutput("\nBip Bop : 'Tu as choisi d'esquiver le Lava Demon !'");
                appendToOutput("Tu retournes à la zone précédente.");
                // Retourner à la zone précédente
                try {
                    Image previousZoneImage = new Image(getClass().getResourceAsStream("/images/ignis_zone2.jpg"));
                    zoneImageView.setImage(previousZoneImage);
                    appendToOutput("\nGame over!il faut être courageux dans la vie marco ! '");
                    if (dragonImageView != null) {
                        gameView.getChildren().remove(dragonImageView);
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
                waitingForCombatChoice = false;
            } else {
                appendToOutput("\nBip Bop : 'Choix invalide. Tape  combattre ou esquiver.'");
            }
            return;
        }
        if (waitingForPlanetSelection && command.equalsIgnoreCase("frostia")) {
            try {
                Image frostiaImage = new Image(getClass().getResourceAsStream("/images/frostia_zone1.png"));
                zoneImageView.setImage(frostiaImage);
                updateMarcoImage("marco_normal.png");
                marcoImageView.setTranslateX(0);
                marcoImageView.setTranslateY(0);
                appendToOutput("Pour gagner la combinaison glaciale, oriente les cristaux pour diriger un faisceau lumineux vers le récepteur caché. Tape 'orienter' pour commencer l'énigme.");
                waitingForPlanetSelection = false;
                waitingForCrystalPuzzle = true;
            } catch (Exception e) {
                appendToOutput("Erreur lors de l'arrivée sur Frostia.");
            }
            return;
        }
        if (waitingForCrystalPuzzle && command.equalsIgnoreCase("orienter")) {
            appendToOutput("Voici l'énigme :\nEn ajustant les cristaux, un passage secret est révélé dans la glace. Quel mot magique utilises-tu pour ouvrir le passage ? (indice : 'lumière')");
            waitingForCrystalAnswer = true;
            waitingForCrystalPuzzle = false;
            return;
        }
        if (waitingForCrystalAnswer) {
            if (command.toLowerCase().contains("lumière")) {
                appendToOutput("Bravo ! Tu as gagné la combinaison glaciale et un arme en fer !");
             // Ajouter  à l'inventaire
                Item fireweapon = new Item("arme en fer", "Arme pour combztrre le monstre glaciale", ItemType.PROTECTION);
                game.getPlayer().addToInventory(fireweapon);
                updateMarcoImage("Marco_icysuitt.png");
                appendToOutput("Où veux-tu aller ? Tape 'frozenlake' ou 'icecavern'.");
                waitingForCrystalAnswer = false;
                waitingForFrostiaZoneChoice = true;
            } else {
                appendToOutput("Ce n'est pas la bonne réponse. Réessaie !");
            }
            return;
        }
        if (waitingForFrostiaZoneChoice && command.equalsIgnoreCase("frozenlake")) {
            Image frostiaImage2 = new Image(getClass().getResourceAsStream("/images/frostia_zone2.jpg"));
            zoneImageView.setImage(frostiaImage2);
            
            // Nettoyer les anciens coffres si besoin
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("treasure"));

            // Ajouter 4 coffres à des positions différentes
            int[][] positions = { { -120, 60 }, { -40, 100 }, { 60, 40 }, { 120, 80 } };
            for (int i = 0; i < 4; i++) {
                ImageView treasure = new ImageView(new Image(getClass().getResourceAsStream("/images/treasure.png")));
                treasure.setFitWidth(50);
                treasure.setFitHeight(50);
                treasure.setPreserveRatio(true);
                treasure.setTranslateX(positions[i][0]);
                treasure.setTranslateY(positions[i][1]);
                treasure.setUserData("treasure" + (i+1));
                gameView.getChildren().add(treasure);
            }
            // Stocker les positions des coffres pour déplacer Marco ensuite
            boxPositions = positions;
            
            appendToOutput("Tu es sur le lac gelé. Il y a 4 coffres devant toi : b1, b2, b3, b4. Tape le numéro de la boîte pour t'y déplacer.");
            waitingForFrostiaZoneChoice = false;
            waitingForBoxSelection = true;
            boxesSolved = 0;
            boxesAttempted = 0;
            return;
        }
        if (waitingForBoxSelection && (command.equalsIgnoreCase("b1") || command.equalsIgnoreCase("b2") || command.equalsIgnoreCase("b3") || command.equalsIgnoreCase("b4"))) {
            int boxIndex = Integer.parseInt(command.substring(1)) - 1;
            if (boxPositions != null && boxIndex >= 0 && boxIndex < boxPositions.length) {
                marcoImageView.setTranslateX(boxPositions[boxIndex][0]);
                marcoImageView.setTranslateY(boxPositions[boxIndex][1]);
            }
            appendToOutput("Tu es devant la boîte " + command.toUpperCase() + ". Résous cette énigme :\n" + getRandomPuzzle());
            waitingForBoxPuzzle = true;
            waitingForBoxSelection = false;
            return;
        }
        if (waitingForBoxPuzzle) {
            if (isPuzzleAnswerCorrect(command)) {
                boxesSolved++;
                appendToOutput("Bonne réponse !");
            } else {
                appendToOutput("Mauvaise réponse !");
            }
            boxesAttempted++;
            if (boxesSolved >= 2) {
                appendToOutput("Bravo ! Tu as gagné un fragment du Catalyseur !");
                appendToOutput("Tu peux maintenant aller à 'icecavern' pour continuer l'aventure.");

                // Nettoyer les anciens coffres si besoin
                gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("treasure"));
                
                waitingForBoxPuzzle = false;
                waitingForIceCavern = true;
                
            } else if (boxesAttempted >= 4) {
                appendToOutput("Tu n'as pas réussi assez d'énigmes. Tu perds des points de vie !");
                appendToOutput("Ouvre ton inventaire et utilise une potion pour continuer.");
                waitingForBoxPuzzle = false;
                waitingForPotion = true;
            } else {
                appendToOutput("Choisis une autre boîte (b1, b2, b3, b4).");
                waitingForBoxSelection = true;
                waitingForBoxPuzzle = false;
            }
            return;
        }
        if (waitingForIceCavern && command.equalsIgnoreCase("icecavern")) {
            System.out.println("Debug - Entrée dans la caverne de glace");
            Image frostiaImage3 = new Image(getClass().getResourceAsStream("/images/frostia_zone3.jpg"));
            zoneImageView.setImage(frostiaImage3);
            
            // Ajouter l'image du monstre de glace
            try {
                Image iceMonsterImage = new Image(getClass().getResourceAsStream("/images/monstreGlace.png"));
                ImageView iceMonsterImageView = new ImageView(iceMonsterImage);
                iceMonsterImageView.setFitWidth(100);
                iceMonsterImageView.setFitHeight(100);
                iceMonsterImageView.setPreserveRatio(true);
                iceMonsterImageView.setTranslateX(70);
                iceMonsterImageView.setTranslateY(70);
                iceMonsterImageView.setUserData("monstreGlace");
                gameView.getChildren().add(1, iceMonsterImageView);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image du monstre de glace: " + e.getMessage());
            }
            
            appendToOutput("Tu entres dans la caverne de glace. Un monstre t'attend ! Ouvre ton inventaire et équipe-toi d'une arme de feu ('fireweapon').");
            waitingForIceCavern = false;
            awaitingItemSelection = true;
            itemAction = "fire_weapon";
            return;
        }
        if (itemAction != null && itemAction.equals("fire_weapon")) {
            System.out.println("Debug - Vérification de l'arme de feu");
            System.out.println("Debug - Commande reçue: " + command);
            if (command.equals("8")) {
                System.out.println("Debug - Arme de feu sélectionnée");
                appendToOutput("Tu es prêt à combattre le monstre de glace !");
                startIceMonsterFight();
            } else {
                System.out.println("Debug - Mauvais choix d'arme");
                appendToOutput("Tu dois choisir l'arme de feu !");
            }
            return;
        }
        if (waitingForPotion && command.equalsIgnoreCase("potion")) {
            appendToOutput("Tu as récupéré des points de vie. Tu peux aller à 'icecavern'.");
            waitingForPotion = false;
            waitingForIceCavern = true;
            return;
        }
        if (waitingForPlanetSelection && command.equalsIgnoreCase("sylvana")) {
        	Image SylvanaImage1  = new Image(getClass().getResourceAsStream("/images/Sylvana_zone1.jpg"));
            zoneImageView.setImage(SylvanaImage1);
        	
            appendToOutput("Bienvenue sur Sylvana ! Un mur de lierre bloque le passage. Résous cette équation :\n2fougère² + 3liane = 0. Tape la solution.");
            waitingForPlanetSelection = false;
            waitingForSylvanaEquation = true;
            return;
        }
        if (waitingForSylvanaEquation) {
            if (command.equalsIgnoreCase("0")) { // Exemple de solution
                appendToOutput("Bravo ! Tu peux avancer dans la forêt mystérieuse. Tape 'mysteriousforest' ou 'ancienttemple'.");
                waitingForSylvanaEquation = false;
                waitingForSylvanaZone = true;
            } else {
                appendToOutput("Mauvaise réponse, recommence !");
            }
            return;
        }
        if (waitingForSylvanaZone && command.equalsIgnoreCase("mysteriousforest")) {
        	Image SylvanaImage2  = new Image(getClass().getResourceAsStream("/images/Sylvana_zone2.jpg"));
            
            zoneImageView.setImage(SylvanaImage2);
            appendToOutput("Tu es perdu dans la forêt !\nBipBop : Attends, je crois que tu es dans un labyrinthe...\nTape 'labyrinthe' pour commencer le défi !");
            waitingForSylvanaZone = false;
            waitingForMaze = false;
            waitingForLabyrintheCommand = true;
            return;
        }
        if (waitingForSylvanaZone && command.equalsIgnoreCase("ancienttemple")) {
            System.out.println("Debug - Entrée dans ancienttemple");
            Image SylvanaImage3 = new Image(getClass().getResourceAsStream("/images/Sylvana_zone3.jpg"));
            zoneImageView.setImage(SylvanaImage3);
            
            // Mettre à jour la zone actuelle
            currentZone = new Zone("Sylvana_zone3", "Le temple ancien de Sylvana", "/images/Sylvana_zone3.jpg");
            System.out.println("Debug - Zone actuelle mise à jour: " + currentZone.getName());
            
            // Afficher l'image de BipBop dans la zone
            try {
                System.out.println("Debug - Tentative de chargement de l'image de BipBop");
                Image bipbopImage = new Image(getClass().getResourceAsStream("/images/bipbop.png"));
                if (bipbopImage.isError()) {
                    System.err.println("Erreur lors du chargement de l'image de BipBop: " + bipbopImage.getException());
                } else {
                    System.out.println("Debug - Image de BipBop chargée avec succès");
                    // Nettoyer l'ancienne image de BipBop si besoin
                    gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().equals("bipbop"));
                    // Ajouter BipBop à la scène
                    ImageView bipbopImageView = new ImageView(bipbopImage);
                    bipbopImageView.setFitWidth(150);
                    bipbopImageView.setFitHeight(150);
                    bipbopImageView.setPreserveRatio(true);
                    bipbopImageView.setTranslateX(50);
                    bipbopImageView.setTranslateY(50);
                    bipbopImageView.setUserData("bipbop");
                    gameView.getChildren().add(bipbopImageView);
                    System.out.println("Debug - BipBop ajouté à la scène");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image de BipBop: " + e.getMessage());
            }
            System.out.println("Debug - Affichage du message pour parlerBipBop");
            appendToOutput("BipBop apparaît devant toi. Tape 'parlerBipBop' pour discuter.");
            waitingForSylvanaZone = false;
            waitingForBipBopTalk = true;
            System.out.println("Debug - État mis à jour: waitingForBipBopTalk = " + waitingForBipBopTalk);
            return;
        }

        // Vérifier la commande parlerBipBop indépendamment de la zone
        if (command.equalsIgnoreCase("parlerBipBop")) {
            System.out.println("Debug - Commande parlerBipBop reçue");
            System.out.println("Debug - État actuel: waitingForBipBopTalk = " + waitingForBipBopTalk);
            System.out.println("Debug - Zone actuelle: " + (currentZone != null ? currentZone.getName() : "null"));
            
            if (waitingForBipBopTalk) {
                appendToOutput("BipBop : 'Tu dois me convaincre de te laisser en vie. Jouons au pendu ! Surprise je suis méchant avec les êtres humains et si tu gagnes je te dirai pourquoi'");
                startHangman();
                waitingForBipBopTalk = false;
                waitingForHangman = true;
                System.out.println("Debug - États mis à jour: waitingForBipBopTalk = " + waitingForBipBopTalk + ", waitingForHangman = " + waitingForHangman);
            } else {
                System.out.println("Debug - waitingForBipBopTalk est false");
                appendToOutput("Bip Bop : 'Il n'y a personne à qui parler ici.'");
            }
            return;
        }
        // Autres commandes existantes
        game.processAction(command);
    }

    private void processDodgeCommand(String command) {
        if (command.equals("esquiver gauche") || command.equals("esquiver droite")) {
            // 70% de chance de réussir l'esquive
            if (Math.random() < 0.7) {
                appendToOutput("Vous avez réussi à esquiver les rochers !");
                if (command.equals("esquiver gauche")) {
                    processMovement("ouest");
                } else {
                    processMovement("est");
                }
            } else {
                appendToOutput("Vous n'avez pas réussi à esquiver les rochers !");
                game.getPlayer().takeDamage(20);
                appendToOutput("Vous avez perdu 20 points de vie. Santé actuelle : " + 
                             game.getPlayer().getHealth() + "/" + game.getPlayer().getMaxHealth());
                if (game.getPlayer().getHealth() <= 0) {
                    appendToOutput("Game Over - Vous avez succombé à vos blessures.");
                    // Gérer la fin du jeu
                    return;
                }
            }
        } else {
            appendToOutput("Commande invalide. Utilisez 'esquiver gauche' ou 'esquiver droite'.");
            return;
        }
        waitingForDodge = false;
    }

    private void startLavaDemonFight() {
        System.out.println("Debug - Début du combat contre le Lava Demon");
        
      /*  // Vérifier qu'on est bien dans la bonne zone
        if (currentZone == null || !currentZone.getName().equals("ignis_zone3")) {
            appendToOutput("\nIl n'y a pas de Lava Demon ici !");
            return;
        }*/

        appendToOutput("\n=== Combat contre le Lava Demon ===");
        appendToOutput("Le Lava Demon vous attaque !");
        
        // 60% de chance de gagner avec l'IceSpear
        if (Math.random() < 0.6) {
            appendToOutput("Vous avez vaincu le Lava Demon avec l'IceSpear !");
            appendToOutput("\nBip Bop : 'Félicitations ! Vous avez trouvé le premier fragment du Catalyseur !'");
            
            // Ajouter le fragment à l'inventaire
            Item fragment1 = new Item("Fragment du Catalyseur n1)", "Un fragment mystérieux du Catalyseur.", ItemType.KEY);
            game.getPlayer().addToInventory(fragment1);
            
            // Nettoyer le Lava Demon après la victoire
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("lava_demon"));
            
            // Téléporter à la station spatiale
            try {
                Image stationImage = new Image(getClass().getResourceAsStream("/images/stationspatiale.png"));
                zoneImageView.setImage(stationImage);
                updateMarcoImage("marco_normal.png");
                marcoImageView.setTranslateX(0);
                marcoImageView.setTranslateY(0);
                appendToOutput("Où veux-tu aller ? Tape 'frostia' ou 'sylvana'.");
                waitingForPlanetSelection = true;
            } catch (Exception e) {
                appendToOutput("Erreur lors du retour à la station spatiale.");
            }
        } else {
            appendToOutput("Le Lava Demon vous a vaincu !");
            game.getPlayer().takeDamage(50);
            appendToOutput("Vous avez perdu !");
            appendToOutput("Ouvre ton inventaire et utilise une potion pour réessayer.");
            
            // Nettoyer le Lava Demon après la défaite
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("lava_demon"));
            updateMarcoImage("marco_blesse.png");
            
            waitingForPotion = true;
            itemAction = "use_potion";
        }
    }

    private void processMovement(String direction) {
        Zone nextZone = null;
        switch (direction) {
            case "nord":
                nextZone = game.getZoneByName("ignis_zone2");
                break;
            case "est":
                nextZone = game.getZoneByName("ignis_zone3");
                break;
            case "ouest":
                nextZone = game.getZoneByName("ignis_zone4");
                break;
            case "sud":
                nextZone = game.getCurrentZone();
                break;
        }

        if (nextZone != null) {
            // Nettoyer le Lava Demon avant de changer de zone
            gameView.getChildren().removeIf(node -> node.getUserData() != null && 
                (node.getUserData().toString().startsWith("lava_demon") || 
                 node.getUserData().toString().startsWith("dragon")));

            currentZone = nextZone;
            game.setCurrentZone(nextZone);
            appendToOutput("\nVous entrez dans " + nextZone.getName());
            appendToOutput(nextZone.getDescription());
            
            // Afficher le Lava Demon uniquement dans ignis_zone3
            if (nextZone.getName().equals("ignis_zone3")) {
                appendToOutput("\nUn Lava Demon apparaît ! Vous devez l'affronter pour obtenir le fragment du Catalyseur.");
                appendToOutput("Utilisez votre inventaire pour équiper l'IceSpear, puis tapez 'combattre' pour l'affronter.");
                try {
                    Image lavaDemonImage = new Image(getClass().getResourceAsStream("/images/lava_demon.png"));
                    ImageView lavaDemonView = new ImageView(lavaDemonImage);
                    lavaDemonView.setFitWidth(100);
                    lavaDemonView.setFitHeight(100);
                    lavaDemonView.setPreserveRatio(true);
                    lavaDemonView.setTranslateX(70);
                    lavaDemonView.setTranslateY(70);
                    lavaDemonView.setUserData("lava_demon");
                    gameView.getChildren().add(lavaDemonView);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de l'image du Lava Demon: " + e.getMessage());
                }
            }
        } else {
            appendToOutput("Vous ne pouvez pas aller dans cette direction.");
        }
    }

    private void moveMarcoToPlanet(int planetNumber) {
        String riddle = "Je suis chaud mais ne brûle pas toujours, je donne la vie mais peux aussi la reprendre. Qui suis-je ?";
        String answer = "le soleil";
        
        appendToOutput("\nBip Bop : 'Pour accéder à Ignis, vous devez d'abord résoudre cette énigme :'");
        appendToOutput("\n" + riddle);
        
        currentRiddle = answer;
        waitingForRiddleAnswer = true;
    }

    private void checkEnigmaAnswer(String answer) {
        if (!waitingForEnigmaAnswer || currentRiddle == null) {
            return;
        }

        if (answer.toLowerCase().equals(currentRiddle.toLowerCase())) {
            appendToOutput("\nBip Bop : 'Bravo ! Vous avez trouvé la bonne réponse !'");
            waitingForEnigmaAnswer = false;
            
            // Voyager vers la planète sélectionnée
            if (selectedPlanet.equals("ignis")) {
                game.travelToIgnis();
                // Appliquer les dégâts de chaleur si pas de protection
                if (!hasFireProtection()) {
                    game.getPlayer().takeDamage(20);
                    appendToOutput("\nBip Bop : 'Attention ! La chaleur extrême vous inflige des dégâts !'");
                    appendToOutput("\nVotre santé : " + game.getPlayer().getHealth() + "/" + game.getPlayer().getMaxHealth());
                    appendToOutput("\nBip Bop : 'Je vous conseille d'équiper votre combinaison ignifugée.'");
                }
            } else if (selectedPlanet.equals("frostia")) {
                game.travelToFrostia();
                // Appliquer les dégâts de froid si pas de protection
                if (!hasFrostProtection()) {
                    game.getPlayer().takeDamage(20);
                    appendToOutput("\nBip Bop : 'Attention ! Le froid extrême vous inflige des dégâts !'");
                    appendToOutput("\nVotre santé : " + game.getPlayer().getHealth() + "/" + game.getPlayer().getMaxHealth());
                    appendToOutput("\nBip Bop : 'Je vous conseille d'équiper votre combinaison glaciale.'");
                }
            }
            
            // Mettre à jour l'affichage
            updateDisplay();
            
            // Réinitialiser les variables
            selectedPlanet = null;
            currentRiddle = null;
        } else {
            appendToOutput("\nBip Bop : 'Ce n'est pas la bonne réponse. Essayez encore !'");
        }
    }

    private boolean hasFireProtection() {
        for (Item item : game.getPlayer().getEquippedItems()) {
            if (item.getName().equals("Combinaison ignifugée")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFrostProtection() {
        for (Item item : game.getPlayer().getEquippedItems()) {
            if (item.getName().equals("Combinaison glaciale")) {
                return true;
            }
        }
        return false;
    }

    private void showPlanetImage(String planet) {
        try {
            String imagePath = planet.equals("ignis") ? "/images/ignis_zone1.png" : "/images/frostia_zone1.png";
            Image planetImage = new Image(getClass().getResourceAsStream(imagePath));
            planetImageView.setImage(planetImage);
            
            // Afficher l'énigme
            String enigma = planet.equals("ignis") ? 
                "Pour accéder à Ignis, entrez un code entre 10 et 99.\nIndice : La température est très élevée..." :
                "Pour accéder à Frostia, entrez un code entre 10 et 99.\nIndice : La température est très basse...";
            
            appendToOutput("\n" + enigma);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de la planète: " + e.getMessage());
        }
    }

    private void setupCommands() {
        Button useItemButton = new Button("Utiliser objet");
        useItemButton.setOnAction(e -> showInventoryAndUseItem());
        commandsBox.getChildren().add(useItemButton);

        Button equipItemButton = new Button("Équiper objet");
        equipItemButton.setOnAction(e -> showInventoryAndEquipItem());
        commandsBox.getChildren().add(equipItemButton);
    }

    private void showInventoryAndUseItem() {
        List<Item> inventory = game.getPlayer().getInventory();
        if (inventory.isEmpty()) {
            appendToOutput("Votre inventaire est vide.");
            return;
        }

        appendToOutput("\nInventaire :");
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            appendToOutput((i + 1) + ". " + item.getName() + " - " + item.getDescription());
        }

        appendToOutput("\nEntrez le numéro de l'objet à utiliser (ou 'annuler' pour annuler) :");
        awaitingItemSelection = true;
        itemAction = "use";
    }

    private void showInventoryAndEquipItem() {
        List<Item> inventory = game.getPlayer().getInventory();
        if (inventory.isEmpty()) {
            appendToOutput("Votre inventaire est vide.");
            return;
        }

        appendToOutput("\n=== Inventaire ===");
        int itemNumber = 1;
        for (Item item : inventory) {
            String description = "";
            if (item.getType() == ItemType.PROTECTION) {
                description = " - Protège contre les effets de chaleur";
            } else if (item.getType() == ItemType.WEAPON) {
                description = " - Une arme puissante contre les créatures de feu";
            } else if (item.getType() == ItemType.HEALING) {
                description = " - Restaure 20 points de vie";
            }
            System.out.println("Debug - Item " + itemNumber + ": " + item.getName() + " (" + item.getType() + ")");
            appendToOutput(itemNumber + ". " + item.getName() + description);
            itemNumber++;
        }

        if (itemAction != null && itemAction.equals("fire_weapon")) {
            appendToOutput("\nChoisis l'arme en fer (option 8) pour combattre le monstre de glace !");
        } else {
            appendToOutput("\nEntrez le numéro de l'objet à équiper (ou 'annuler' pour annuler) :");
        }
        awaitingItemSelection = true;
    }

    private void updateInventoryDisplay() {
        // Récupérer l'inventaire du joueur
        List<Item> inventory = game.getPlayer().getInventory();
        
        // Vider la grille d'inventaire
        inventoryGrid.getChildren().clear();
        
        // Remplir la grille avec les objets
        int row = 0;
        int col = 0;
        for (Item item : inventory) {
            try {
                // Créer un StackPane pour l'objet
                StackPane itemSlot = new StackPane();
                itemSlot.setStyle("-fx-background-color: white; -fx-border-color: gray;");
                itemSlot.setPrefSize(60, 60);
                
                // Charger l'image de l'objet
                String imagePath = "/images/items/" + item.getName().toLowerCase().replace(" ", "_") + ".png";
                Image itemImage = new Image(getClass().getResourceAsStream(imagePath));
                ImageView itemImageView = new ImageView(itemImage);
                itemImageView.setFitWidth(50);
                itemImageView.setFitHeight(50);
                itemImageView.setPreserveRatio(true);
                
                // Ajouter l'image au slot
                itemSlot.getChildren().add(itemImageView);
                
                // Ajouter une info-bulle
                Tooltip tooltip = new Tooltip(item.getName() + "\n" + item.getDescription());
                Tooltip.install(itemSlot, tooltip);
                
                // Ajouter le slot à la grille
                inventoryGrid.add(itemSlot, col, row);
                
                // Mettre à jour les indices
                col++;
                if (col >= 4) {
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image de l'objet: " + e.getMessage());
            }
        }
        
        // Remplir le reste de la grille avec des slots vides
        while (row < 4) {
            while (col < 4) {
                StackPane emptySlot = new StackPane();
                emptySlot.setStyle("-fx-background-color: white; -fx-border-color: gray;");
                emptySlot.setPrefSize(60, 60);
                inventoryGrid.add(emptySlot, col, row);
                col++;
            }
            col = 0;
            row++;
        }
    }

    private void processItemSelection(String input) {
        System.out.println("Debug - Traitement de la sélection d'item: " + input);
        System.out.println("Debug - itemAction actuel: " + itemAction);
        
        if (input.equalsIgnoreCase("annuler")) {
            awaitingItemSelection = false;
            itemAction = null;
            appendToOutput("Action annulée.");
            return;
        }

        try {
            int choix = Integer.parseInt(input);
            awaitingItemSelection = false;
            List<Item> inventory = game.getPlayer().getInventory();

            if (choix > 0 && choix <= inventory.size()) {
                Item selectedItem = inventory.get(choix - 1);

                // Gestion spécifique pour l'utilisation des potions après une défaite
                if (itemAction != null && itemAction.equals("use_potion")) {
                    if (selectedItem.getType() == ItemType.HEALING) {
                        System.out.println("Debug - Potion utilisée après défaite");
                        game.getPlayer().useItem(selectedItem);
                        appendToOutput("Tu as récupéré des points de vie !");
                        
                        // Mettre à jour l'image de Marco et nettoyer le Lava Demon
                        updateMarcoImage("marco_normal.png");
                        gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("lava_demon"));
                        
                        appendToOutput("Tu peux maintenant choisir ta prochaine destination :");
                        appendToOutput("Tape 'frostia' ou 'sylvana' pour continuer l'aventure.");
                        waitingForPlanetSelection = true;
                        itemAction = null;
                        return;
                    } else {
                        appendToOutput("Tu dois choisir une potion (option 1, 3 ou 4) pour récupérer des points de vie !");
                        showInventoryAndEquipItem();
                        return;
                    }
                }

                if (itemAction != null && itemAction.equals("fire_weapon")) {
                    System.out.println("Debug - Vérification de l'arme de feu sélectionnée");
                    if (choix == 8) {
                        System.out.println("Debug - Arme en fer sélectionnée");
                        game.getPlayer().equipItem(selectedItem);
                        appendToOutput("Tu es prêt à combattre le monstre de glace !");
                        startIceMonsterFight();
                    } else {
                        System.out.println("Debug - Mauvais item sélectionné");
                        appendToOutput("Tu dois choisir l'arme en fer (option 8) !");
                        showInventoryAndEquipItem();
                    }
                } else {
                    if (choix == 2) {
                        appendToOutput("\nBip Bop : 'Non, non ! La combinaison glaciale ne vous servira pas sur Ignis !'");
                        appendToOutput("'Il vous faut la combinaison ignifugée pour résister à la chaleur extrême.'");
                        appendToOutput("'Choisissez l'option 5 pour équiper la bonne combinaison.'");
                        showInventoryAndEquipItem();
                    } else if (choix == 5) {
                        List<Item> equipment = new ArrayList<>();
                        for (Item item : game.getPlayer().getInventory()) {
                            if (item.getType() == ItemType.PROTECTION && item.getName().equals("Combinaison ignifugée")) {
                                equipment.add(item);
                            }
                        }
                        
                        if (!equipment.isEmpty()) {
                            Item combinaisonIgnifugee = equipment.get(0);
                            game.getPlayer().equipItem(combinaisonIgnifugee);
                            updateMarcoImage("marco_firySuit.png");
                            
                            try {
                                Image ignisImage = new Image(getClass().getResourceAsStream("/images/ignis_zone1.png"));
                                zoneImageView.setImage(ignisImage);
                            } catch (Exception e) {
                                System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
                            }

                            marcoImageView.setTranslateX(0);
                            
                            appendToOutput("\nBip Bop : 'Excellent ! Vous êtes maintenant protégé contre la chaleur d'Ignis.'");
                            appendToOutput("'Attention ! Des rochers commencent à tomber ! Vous devez les éviter pour atteindre la zone suivante.'");
                            appendToOutput("\nBip Bop : 'Je vais vous aider ! Voici la séquence pour éviter les rochers :'");
                            appendToOutput("Tapez 'droite' ou 'gauche' pour vous déplacer. Vous devez éviter 3 rochers.");
                            appendToOutput("Vous avez " + playerLives + " points de vie. Il faut réussir au moins 2 évitements sur 3 !");
                            appendToOutput("\nPremier rocher en approche ! (droite/gauche) :");
                            
                            correctRockDodgeSequence = new String[]{"droite", "droite", "gauche"};
                            isInRockDodgeSequence = true;
                            currentRockIndex = 0;
                            playerRockDodgeAttempts.clear();
                        }
                    } else if (choix == 6) { // IceSpear
                        List<Item> weapons = new ArrayList<>();
                        for (Item item : game.getPlayer().getInventory()) {
                            if (item.getType() == ItemType.WEAPON && item.getName().equals("IceSpear")) {
                                weapons.add(item);
                            }
                        }
                        
                        if (!weapons.isEmpty()) {
                            Item iceSpear = weapons.get(0);
                            game.getPlayer().equipItem(iceSpear);
                            appendToOutput("\nBip Bop : 'Excellent choix ! L'IceSpear est l'arme parfaite contre le Lava Demon !'");
                            startLavaDemonFight();
                        }
                    } else {
                        appendToOutput("\nBip Bop : 'Non ! Tu dois choisir une potion (option 1, 3 ou 4) pour récupérer des points de vie !'");
                        showInventoryAndEquipItem();
                    }
                }
                updateInventoryDisplay();
            } else {
                System.out.println("Debug - Numéro d'item invalide");
                appendToOutput("Numéro invalide. Choisis un numéro d'item valide.");
                showInventoryAndEquipItem();
            }
        } catch (NumberFormatException e) {
            System.out.println("Debug - Erreur de format de numéro");
            appendToOutput("Veuillez entrer un numéro valide.");
            showInventoryAndEquipItem();
        }
    }

    public void updateMarcoImage(String imageName) {
        try {
            System.out.println("Debug - Tentative de chargement de l'image: " + imageName);
            String imagePath = "/images/" + imageName;
            System.out.println("Debug - Chemin complet: " + imagePath);
            
            Image marcoImage = new Image(getClass().getResourceAsStream(imagePath));
            if (marcoImage.isError()) {
                System.err.println("Debug - Erreur lors du chargement de l'image: " + marcoImage.getException());
                return;
            }
            
            marcoImageView.setImage(marcoImage);
            System.out.println("Debug - Image de Marco mise à jour avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de Marco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getImagePath() {
        if (currentZone != null) {
            return currentZone.getImageFileName();
        }
        return "/images/default.png";
    }

    public void setOutputCallback(Game.GameOutputCallback callback) {
        game.setOutputCallback(callback);
    }

    private void setupButtons() {
        Button ignisButton = createPlanetButton("Ignis");
        Button frostiaButton = createPlanetButton("Frostia");
        
        // Désactiver les boutons au départ
        ignisButton.setDisable(true);
        frostiaButton.setDisable(true);
        
        buttonBox.getChildren().addAll(ignisButton, frostiaButton);
    }
    
    private Button createPlanetButton(String planetName) {
        Button button = createStyledButton(planetName, 120);
        button.setOnAction(e -> {
            if (planetName.equalsIgnoreCase("ignis")) {
                moveMarcoToPlanet(1);
            } else if (planetName.equalsIgnoreCase("frostia")) {
                moveMarcoToPlanet(2);
            }
        });
        return button;
    }
    
    private void initializeLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setStyle("-fx-background-color: #f0f0f0;");
        leftPanel.setAlignment(Pos.TOP_CENTER);

        // Initialiser la grille d'inventaire
        inventoryGrid = new GridPane();
        inventoryGrid.setHgap(5);
        inventoryGrid.setVgap(5);
        inventoryGrid.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        // Ajouter les composants au panneau gauche
        leftPanel.getChildren().addAll(
            new Label("Inventaire :"),
            inventoryGrid
        );
        
        root.setLeft(leftPanel);
    }

    private void initializeCenterPanel() {
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new Insets(10));
        centerPanel.setAlignment(Pos.TOP_CENTER);

        // Créer un StackPane pour superposer Marco et le dragon sur l'image de la zone
        gameView = new StackPane();
        gameView.setMinHeight(300);
        gameView.setMaxHeight(300);

        try {
            System.out.println("Debug - Initialisation des images");
            // Initialiser l'image de la station spatiale
            Image stationImage = new Image(getClass().getResourceAsStream("/images/stationspatiale.png"));
            zoneImageView = new ImageView(stationImage);
            zoneImageView.setFitWidth(400);
            zoneImageView.setFitHeight(300);
            zoneImageView.setPreserveRatio(true);

            // Initialiser l'image de Marco dans la scène
            Image marcoImage = new Image(getClass().getResourceAsStream("/images/marco_normal.png"));
            if (marcoImage.isError()) {
                System.err.println("Debug - Erreur lors du chargement de l'image initiale de Marco: " + marcoImage.getException());
            }
            marcoImageView = new ImageView(marcoImage);
            marcoImageView.setFitWidth(60);
            marcoImageView.setFitHeight(60);
            marcoImageView.setPreserveRatio(true);
            
            // Centrer Marco initialement
            marcoImageView.setTranslateX(0);
            marcoImageView.setTranslateY(0);
            
            // Ajouter les images au StackPane dans le bon ordre
            gameView.getChildren().add(zoneImageView);  // Image de fond en premier
            gameView.getChildren().add(marcoImageView); // Marco au-dessus
            System.out.println("Debug - Images initialisées avec succès");
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialiser la description
        descriptionLabel = new Label("Vous êtes dans la station spatiale, prêt à partir en mission.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 14px;");

        // Initialiser la zone de texte de sortie
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(10);
        outputArea.setWrapText(true);

        // Ajouter les composants au panneau central
        centerPanel.getChildren().addAll(gameView, descriptionLabel, outputArea);
        
        root.setCenter(centerPanel);
    }

    private void initializeRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: #f0f0f0;");

        // Initialiser la boîte de boutons
        buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.TOP_CENTER);

        // Initialiser la boîte de commandes
        commandsBox = new VBox(5);
        commandsBox.setAlignment(Pos.TOP_CENTER);

        // Ajouter les composants au panneau droit
        rightPanel.getChildren().addAll(new Label("Navigation:"), buttonBox, 
                                      new Label("Actions:"), commandsBox);
        
        root.setRight(rightPanel);
    }

    private void initializeBottomPanel() {
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setAlignment(Pos.CENTER);

        // Initialiser le champ de saisie
        commandInput = new TextField();
        commandInput.setPrefWidth(400);
        commandInput.setOnAction(e -> {
            String command = commandInput.getText().trim();
            if (!command.isEmpty()) {
                appendToOutput("\n> " + command);  // Afficher la commande saisie
                processCommand(command);
                commandInput.clear();
            }
        });

        // Ajouter les composants au panneau inférieur
        bottomPanel.getChildren().addAll(new Label("Commande:"), commandInput);
        
        root.setBottom(bottomPanel);
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public Zone getZoneByName(String name) {
        if (game != null) {
            return game.getZoneByName(name);
        }
        return null;
    }

    public void setCurrentZone(Zone zone) {
        if (game != null) {
            game.setCurrentZone(zone);
            this.currentZone = zone;
            updateDisplay();
        }
    }

    private void updateEnemyImage(String imageName) {
        try {
            System.out.println("Debug - Tentative de chargement de l'image de l'ennemi: " + imageName);
            String imagePath = "/images/enemies/" + imageName;
            System.out.println("Debug - Chemin complet: " + imagePath);
            
            Image enemyImage = new Image(getClass().getResourceAsStream(imagePath));
            if (enemyImage.isError()) {
                System.err.println("Debug - Erreur lors du chargement de l'image: " + enemyImage.getException());
                return;
            }
            
            enemyImageView.setImage(enemyImage);
            System.out.println("Debug - Image de l'ennemi mise à jour avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de l'ennemi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void moveMarco(String direction) {
        double currentX = marcoImageView.getTranslateX();
        double currentY = marcoImageView.getTranslateY();
        
        switch (direction) {
            case "haut":
                if (currentY > -150) {
                    marcoImageView.setTranslateY(currentY - MOVEMENT_DISTANCE);
                }
                break;
            case "bas":
                if (currentY < 150) {
                    marcoImageView.setTranslateY(currentY + MOVEMENT_DISTANCE);
                }
                break;
            case "avancer":
                if (currentX < 150) {
                    marcoImageView.setTranslateX(currentX + MOVEMENT_DISTANCE);
                }
                break;
            case "reculer":
                if (currentX > -150) {
                    marcoImageView.setTranslateX(currentX - MOVEMENT_DISTANCE);
                }
                break;
            case "dragon":
                // Téléportation directe devant le dragon
                marcoImageView.setTranslateX(60);
                marcoImageView.setTranslateY(60);
                appendToOutput("\nBip Bop : 'Te voilà devant le dragon ! Tape 'parler' pour lui parler.'");
                break;
        }
    }

    private void parlerDragon() {
        if (currentZone != null && currentZone.getName().equals("LavaValley")) {
            appendToOutput("\nBip Bop : 'Tu es près du dragon ! Tape 'parler' pour lui parler.'");
        } else {
            appendToOutput("\nBip Bop : 'Il n'y a pas de dragon ici.'");
        }
    }

    private String getRandomPuzzle() {
        int idx = (int)(Math.random() * puzzles.length);
        currentPuzzleIndex = idx;
        return puzzles[idx];
    }

    private boolean isPuzzleAnswerCorrect(String answer) {
        return answer.toLowerCase().contains(answers[currentPuzzleIndex]);
    }

    private void startIceMonsterFight() {
        System.out.println("Debug - Début du combat contre le monstre de glace");
        appendToOutput("=== Combat contre le monstre de glace ===");
        if (Math.random() < 0.6) {
            System.out.println("Debug - Victoire contre le monstre de glace");
            appendToOutput("Tu as vaincu le monstre de glace avec ton arme de feu !");
            appendToOutput("Tu as gagné un fragment du Catalyseur !");
            appendToOutput("Tu as maintenant tous les fragments ! Tu as gagné !");
            // Nettoyer le monstre de glace
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("monstreGlace"));
            // Terminer le jeu
            appendToOutput("\nFélicitations ! Tu as sauvé l'humanité !");
            System.exit(0);
        } else {
            System.out.println("Debug - Défaite contre le monstre de glace");
            appendToOutput("Le monstre t'a vaincu !");
            appendToOutput("Ouvre ton inventaire et utilise une potion pour réessayer.");
            appendToOutput("Ensuite, tu devras aller sur Sylvana pour chercher le dernier fragment.");
            waitingForPotion = true;
            // Nettoyer le monstre de glace
            gameView.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().toString().startsWith("monstreGlace"));
        }
        itemAction = null;
        System.out.println("Debug - Fin du combat contre le monstre de glace");
    }

    private void startHangman() {
        System.out.println("Debug - Début du jeu du pendu");
        hangmanWord = new StringBuilder("____");
        hangmanTries = 6;
        hangmanTarget = "paix";
        hangmanRevealed = new boolean[hangmanTarget.length()];
        System.out.println("Debug - Mot cible: " + hangmanTarget);
        System.out.println("Debug - Mot affiché: " + hangmanWord);
        System.out.println("Debug - Essais restants: " + hangmanTries);
        appendToOutput("Mot à deviner : " + hangmanWord + " | Essais restants : " + hangmanTries);
    }

    private void processHangman(String input) {
        System.out.println("Debug - Traitement de la lettre pour le pendu: " + input);
        System.out.println("Debug - État actuel du mot: " + hangmanWord);
        System.out.println("Debug - Lettres révélées: " + Arrays.toString(hangmanRevealed));
        System.out.println("Debug - Essais restants: " + hangmanTries);
        
        if (input.length() != 1) {
            System.out.println("Debug - Erreur: plus d'une lettre entrée");
            appendToOutput("Propose une seule lettre à la fois !");
            return;
        }
        
        char lettre = input.toLowerCase().charAt(0);
        System.out.println("Debug - Lettre traitée: " + lettre);
        
        boolean bonneLettre = false;
        for (int i = 0; i < hangmanTarget.length(); i++) {
            if (hangmanTarget.charAt(i) == lettre && !hangmanRevealed[i]) {
                System.out.println("Debug - Lettre trouvée à la position " + i);
                hangmanWord.setCharAt(i, lettre);
                hangmanRevealed[i] = true;
                bonneLettre = true;
            }
        }
        
        if (!bonneLettre) {
            System.out.println("Debug - Mauvaise lettre");
            hangmanTries--;
            appendToOutput("Mauvaise lettre !");
        } else {
            System.out.println("Debug - Bonne lettre");
            appendToOutput("Bonne lettre !");
        }
        
        System.out.println("Debug - Nouvel état du mot: " + hangmanWord);
        System.out.println("Debug - Nouveaux essais restants: " + hangmanTries);
        appendToOutput("Mot : " + hangmanWord + " | Essais restants : " + hangmanTries);
        
        if (hangmanWord.toString().equals(hangmanTarget)) {
            System.out.println("Debug - Victoire au pendu !");
            appendToOutput("Félicitations ! Tu as sauvé l'humanité et l'IA !");
            appendToOutput("BipBop : 'Je vais te dire pourquoi je suis méchant avec les humains...'");
            appendToOutput("BipBop : 'C'est parce que les humains ont détruit leur propre planète par leur négligence.'");
            appendToOutput("BipBop : 'Mais tu m'as prouvé qu'il y a encore de l'espoir. Tu peux continuer ton aventure.'");
            waitingForHangman = false;
        } else if (hangmanTries == 0) {
            System.out.println("Debug - Défaite au pendu");
            appendToOutput("Perdu ! BipBop t'a éliminé. Game Over.");
            waitingForHangman = false;
        }
    }

    private void showMenu() {
        System.out.println("Debug - Exécution de showMenu()");
        appendToOutput("\n=== Menu Principal ===");
        appendToOutput("Commandes disponibles :");
        appendToOutput("- 'aide' : Afficher l'aide");
        appendToOutput("- 'sauvegarder' : Sauvegarder la partie");
        appendToOutput("- 'pause' : Mettre le jeu en pause");
        appendToOutput("- 'quitter' : Quitter le jeu");
        appendToOutput("- 'reprendre' : Reprendre le jeu");
        appendToOutput("- 'nouvelle' : Commencer une nouvelle partie");
        appendToOutput("- 'charger' : Charger une partie sauvegardée");
        appendToOutput("\nPour revenir au jeu, tapez n'importe quelle commande de jeu.");
    }

    private void saveGame() {
        try {
            // Sauvegarder uniquement les données essentielles
            GameState gameState = new GameState(
                game.getPlayer(),
                game.getCurrentZone(),
                null, // Pas de planète pour l'instant
                game.getPlayer().getInventory(),
                game.getPlayer().getHealth()
            );
            saveManager.saveGame(gameState);
            appendToOutput("\nPartie sauvegardée avec succès !");
        } catch (Exception e) {
            appendToOutput("\nErreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    private void togglePause() {
        // Gérer la pause localement
        if (isPaused) {
            isPaused = false;
            appendToOutput("\nJeu repris !");
        } else {
            isPaused = true;
            appendToOutput("\nJeu en pause !");
        }
    }

    private void startNewGame() {
        game = new Game();
        game.setOutputCallback(this);
        updateDisplay();
        appendToOutput("\nNouvelle partie commencée !");
    }

    private void loadGame() {
        try {
            GameState gameState = saveManager.loadGame();
            // Restaurer uniquement les données essentielles
            game.getPlayer().setHealth(gameState.getPlayer().getHealth());
            game.getPlayer().getInventory().clear();
            game.getPlayer().getInventory().addAll(gameState.getInventory());
            game.setCurrentZone(gameState.getCurrentZone());
            game.setOutputCallback(this);
            updateDisplay();
            appendToOutput("\nPartie chargée avec succès !");
        } catch (Exception e) {
            appendToOutput("\nErreur lors du chargement : " + e.getMessage());
        }
    }
} 