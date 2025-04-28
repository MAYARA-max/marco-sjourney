package com.marcosjourney.model.quest;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomPuzzle {
    private static final Random random = new Random();
    
    private static final List<Puzzle> easyPuzzles = new ArrayList<>();
    
    static {
        // Énigmes mathématiques simples
        easyPuzzles.add(new Puzzle(
            "Addition Magique",
            "Marco voit 2 dragons et 3 phénix. Combien y a-t-il de créatures au total ?",
            "5",
            "Bravo ! Vous avez bien compté les créatures !",
            "Ce n'est pas ça... Essayez de recompter !"
        ));
        
        // Énigmes de couleurs
        easyPuzzles.add(new Puzzle(
            "Mélange de Couleurs",
            "Quelle couleur obtient-on en mélangeant BLEU et JAUNE ?\n1) Rouge\n2) Vert\n3) Orange",
            "2",
            "Exact ! Le bleu et le jaune donnent du vert !",
            "Non, pensez aux mélanges de peinture !"
        ));
        
        // Devinettes simples
        easyPuzzles.add(new Puzzle(
            "Devinette du Dragon",
            "Je crache du feu mais je ne suis pas un volcan,\nJ'ai des ailes mais je ne suis pas un oiseau.\nQui suis-je ?\n1) Un dragon\n2) Une torche\n3) Un phénix",
            "1",
            "Oui, c'est bien le dragon !",
            "Non, pensez à une créature légendaire qui vole et crache du feu..."
        ));
        
        // Énigmes de logique simple
        easyPuzzles.add(new Puzzle(
            "Suite Logique",
            "Complétez la suite : 2, 4, 6, ?",
            "8",
            "Parfait ! La suite continue de 2 en 2 !",
            "Indice : On ajoute toujours le même nombre..."
        ));
        
        // Énigmes d'observation
        easyPuzzles.add(new Puzzle(
            "Compte des Étoiles",
            "Combien y a-t-il d'étoiles dans ce dessin ?\n★ ⭐ ★ ⭐ ★",
            "5",
            "Excellent comptage !",
            "Regardez bien, comptez une par une..."
        ));
        
        // Énigmes de mots
        easyPuzzles.add(new Puzzle(
            "Mot Caché",
            "Quel mot peut-on former avec ces lettres : L A V E ?",
            "vale",
            "Bien joué ! VALE est correct !",
            "Indice : C'est une vallée en anglais..."
        ));
        
        // Énigmes de choix
        easyPuzzles.add(new Puzzle(
            "Choix du Héros",
            "Pour traverser une rivière de lave, vaut-il mieux :\n1) Un pont de pierre\n2) Un pont de bois\n3) Un pont de glace",
            "1",
            "Exact ! La pierre résiste à la lave !",
            "Réfléchissez à ce qui résiste le mieux à la chaleur..."
        ));
        
        // Énigmes de séquence
        easyPuzzles.add(new Puzzle(
            "Séquence Élémentaire",
            "Complétez : Feu → Terre → Eau → ?",
            "air",
            "Oui ! Les 4 éléments sont : Feu, Terre, Eau et Air !",
            "Pensez aux 4 éléments naturels..."
        ));
    }
    
    public static Puzzle getRandomPuzzle() {
        int index = random.nextInt(easyPuzzles.size());
        return easyPuzzles.get(index);
    }

	
} 