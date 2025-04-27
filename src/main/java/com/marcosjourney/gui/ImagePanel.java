package com.marcosjourney.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 300;

    public ImagePanel() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(Color.BLACK);
    }

    public void chargerImage(String nomImage) {
        try {
            String cheminImage = "/images/" + nomImage;
            InputStream is = getClass().getResourceAsStream(cheminImage);
            if (is != null) {
                image = ImageIO.read(is);
                repaint();
            } else {
                System.err.println("Image non trouvée : " + nomImage);
                image = null;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Calcul des dimensions pour maintenir le ratio
            double ratio = Math.min(
                (double) getWidth() / image.getWidth(),
                (double) getHeight() / image.getHeight()
            );
            
            int newWidth = (int) (image.getWidth() * ratio);
            int newHeight = (int) (image.getHeight() * ratio);
            
            // Centrage de l'image
            int x = (getWidth() - newWidth) / 2;
            int y = (getHeight() - newHeight) / 2;
            
            g.drawImage(image, x, y, newWidth, newHeight, this);
        } else {
            // Affichage d'un message si pas d'image
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String message = "Aucune image disponible";
            FontMetrics fm = g.getFontMetrics();
            int messageWidth = fm.stringWidth(message);
            int messageHeight = fm.getHeight();
            g.drawString(message, 
                (getWidth() - messageWidth) / 2,
                (getHeight() - messageHeight) / 2 + fm.getAscent());
        }
    }
} 