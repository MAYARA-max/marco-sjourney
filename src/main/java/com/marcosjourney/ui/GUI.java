package com.marcosjourney.ui;

import javafx.application.Application;

public class GUI {
    private GameUI gameUI;

    public GUI() {
        this.gameUI = null;
    }

    public void start() {
        Application.launch(GameUI.class);
    }

    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    public GameUI getGameUI() {
        return gameUI;
    }
} 