package com.marcosjourney.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.marcosjourney.game.Game;

public class GUI {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField inputField;
    private JLabel imageLabel;
    private JLabel statsLabel;
    private Game game;
    private JPanel mainPanel;
    private JPanel imagePanel;
    private JPanel textPanel;
    private JPanel inputPanel;
    private JPanel statsPanel;
    private InventoryPanel inventoryPanel;
    private boolean isInventoryVisible;

    public GUI(Game game) {
        this.game = game;
        this.isInventoryVisible = false;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Configuration de base de la fenêtre
        frame = new JFrame("Marco's Journey");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout());

        // Création des panneaux
        mainPanel = new JPanel(new BorderLayout());
        imagePanel = new JPanel();
        textPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new BorderLayout());
        statsPanel = new JPanel(new BorderLayout());
        
        // Configuration du panneau d'image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(400, 300));

        // Configuration du panneau de texte
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        // Configuration du panneau de saisie
        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String command = inputField.getText();
            inputField.setText("");
            processCommand(command);
        });
        inputPanel.add(inputField, BorderLayout.CENTER);

        // Configuration du panneau de statistiques
        statsLabel = new JLabel();
        statsLabel.setHorizontalAlignment(JLabel.CENTER);
        statsPanel.add(statsLabel, BorderLayout.CENTER);

        // Configuration du panneau d'inventaire
        inventoryPanel = new InventoryPanel(game.getPlayer());
        inventoryPanel.setPreferredSize(new Dimension(300, frame.getHeight()));
        inventoryPanel.setVisible(false);

        // Assemblage des panneaux
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(imagePanel, BorderLayout.NORTH);
        centerPanel.add(textPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(inventoryPanel, BorderLayout.EAST);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(statsPanel, BorderLayout.NORTH);

        // Ajout des raccourcis clavier
        KeyStroke inventoryKey = KeyStroke.getKeyStroke(KeyEvent.VK_I, 0);
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(inventoryKey, "toggleInventory");
        mainPanel.getActionMap().put("toggleInventory", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleInventory();
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void processCommand(String command) {
        if (command.equalsIgnoreCase("inventaire")) {
            toggleInventory();
        } else {
            game.processCommand(command);
        }
    }

    private void toggleInventory() {
        isInventoryVisible = !isInventoryVisible;
        inventoryPanel.setVisible(isInventoryVisible);
        if (isInventoryVisible) {
            inventoryPanel.updateInventory();
            displayInventoryHelp();
        }
        frame.pack();
    }

    private void displayInventoryHelp() {
        String helpText = "\n=== AIDE INVENTAIRE ===\n" +
                         "- Utilisez la touche 'I' ou tapez 'inventaire' pour ouvrir/fermer l'inventaire\n" +
                         "- Survolez un objet avec la souris pour voir sa description\n" +
                         "- Cliquez sur un objet pour afficher les actions possibles (utiliser/examiner)\n" +
                         "=====================\n\n";
        appendToOutput(helpText);
    }

    public void display() {
        frame.setVisible(true);
    }

    public void appendToOutput(String text) {
        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void updateImage(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel.setIcon(null);
            appendToOutput("Image non trouvée : " + imagePath + "\n");
        }
    }

    public void updateStats(int health, String location, int intelligence) {
        statsLabel.setText("Vie: " + health + " | Zone: " + location + " | Intelligence: " + intelligence);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void clearOutput() {
        textArea.setText("");
    }
} 