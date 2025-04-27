package com.marcosjourney.utils;

import com.marcosjourney.model.GameState;
import java.io.*;

public class SaveManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE = "game_save.dat";

    public SaveManager() {
        createSaveDirectory();
    }

    private void createSaveDirectory() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveGame(GameState gameState) throws IOException {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            oos.writeObject(gameState);
        }
    }

    public GameState loadGame() throws IOException, ClassNotFoundException {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        if (!saveFile.exists()) {
            throw new FileNotFoundException("Aucune sauvegarde trouvée.");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (GameState) ois.readObject();
        }
    }

    public boolean saveExists() {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        return saveFile.exists();
    }

    public void deleteSave() {
        File saveFile = new File(SAVE_DIRECTORY, SAVE_FILE);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }
} 