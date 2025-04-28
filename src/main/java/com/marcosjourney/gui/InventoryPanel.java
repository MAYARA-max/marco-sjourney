package com.marcosjourney.gui;


import javax.swing.*;

import com.marcosjourney.model.items.Item;
import com.marcosjourney.model.player.Player;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private Player player;
    private JPanel itemsGrid;
    private JLabel selectedItemInfo;
    private static final int SLOT_SIZE = 50;
    private static final int GRID_COLS = 5;
    private static final int GRID_ROWS = 4;

    public InventoryPanel(Player player) {
        this.player = player;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Inventaire"));

        // Création de la grille d'inventaire
        itemsGrid = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, 5, 5));
        initializeInventorySlots();

        // Panel d'information sur l'objet sélectionné
        selectedItemInfo = new JLabel("Sélectionnez un objet pour voir ses détails");
        selectedItemInfo.setHorizontalAlignment(JLabel.CENTER);

        add(itemsGrid, BorderLayout.CENTER);
        add(selectedItemInfo, BorderLayout.SOUTH);
    }

    private void initializeInventorySlots() {
        // Création des slots d'inventaire vides
        for (int i = 0; i < GRID_ROWS * GRID_COLS; i++) {
            JPanel slot = createInventorySlot();
            itemsGrid.add(slot);
        }
    }

    private JPanel createInventorySlot() {
        JPanel slot = new JPanel();
        slot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        slot.setBackground(new Color(230, 230, 230));
        return slot;
    }

    public void updateInventory() {
        itemsGrid.removeAll();
        List<Item> inventory = player.getInventory();

        // Afficher les objets de l'inventaire
        for (Item item : inventory) {
            JPanel slot = createItemSlot(item);
            itemsGrid.add(slot);
        }

        // Ajouter des slots vides pour remplir la grille
        for (int i = inventory.size(); i < GRID_ROWS * GRID_COLS; i++) {
            itemsGrid.add(createInventorySlot());
        }

        revalidate();
        repaint();
    }

    private JPanel createItemSlot(Item item) {
        JPanel slot = createInventorySlot();
        
        // Créer l'icône de l'objet
        ImageIcon icon = loadItemIcon(item);
        JLabel iconLabel = new JLabel(icon);
        slot.add(iconLabel);

        // Ajouter les interactions
        slot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selectedItemInfo.setText(item.getName() + " - " + item.getDescription());
                slot.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selectedItemInfo.setText("Sélectionnez un objet pour voir ses détails");
                slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Clic gauche
                    showItemOptions(item, e.getComponent());
                }
            }
        });

        return slot;
    }

    private ImageIcon loadItemIcon(Item item) {
        // Charger l'icône de l'objet
        ImageIcon icon = new ImageIcon(item.getImagePath());
        // Redimensionner l'icône pour qu'elle rentre dans le slot
        Image image = icon.getImage().getScaledInstance(SLOT_SIZE - 10, SLOT_SIZE - 10, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private void showItemOptions(Item item, Component component) {
        JPopupMenu menu = new JPopupMenu();
        
        JMenuItem useItem = new JMenuItem("Utiliser");
        useItem.addActionListener(e -> {
            item.use(player);
            updateInventory();
        });
        
        JMenuItem examineItem = new JMenuItem("Examiner");
        examineItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                item.getDescription(),
                item.getName(),
                JOptionPane.INFORMATION_MESSAGE);
        });

        menu.add(useItem);
        menu.add(examineItem);
        
        menu.show(component, 0, component.getHeight());
    }
} 