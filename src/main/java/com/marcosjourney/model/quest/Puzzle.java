package com.marcosjourney.model.quest;

import java.util.Random;
import java.util.function.Function;

import com.marcosjourney.model.player.Player;

public class Puzzle {
    private String name;
    private String question;
    private String answer;
    private String successMessage;
    private String failureMessage;
    private boolean solved;
    private Function<Player, Void> onSolve;
    private static final Random random = new Random();

    public Puzzle(String name, String question, String answer, String successMessage, String failureMessage) {
        this.name = name;
        this.question = question;
        this.answer = answer;
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
        this.solved = false;
    }

    public Puzzle(String question, String answer, String hint, Function<Player, Void> onSolve) {
        this.question = question;
        this.answer = answer;
        this.onSolve = onSolve;
        this.solved = false;
    }

    public static Puzzle createRandomPuzzle() {
        int puzzleNumber = random.nextInt(10);
        switch (puzzleNumber) {
            case 0:
                return new Puzzle(
                    "Énigme aléatoire",
                    "Quel est le résultat de 2 + 2 ?",
                    "4",
                    "Correct ! Vous pouvez passer.",
                    "Incorrect. Essayez encore."
                );
            case 1:
                return new Puzzle(
                    "Je suis toujours devant toi, mais tu ne peux jamais me toucher. Qui suis-je ?",
                    "demain",
                    "Pense au temps qui passe...",
                    (Player player) -> {
                        System.out.println("Bravo ! Tu as trouvé la réponse !");
                        return null;
                    }
                );
            case 2:
                return new Puzzle(
                    "Plus on me prend, plus on me laisse. Qui suis-je ?",
                    "empreinte",
                    "C'est quelque chose qui reste après ton passage...",
                    player -> {
                        System.out.println("Excellent ! Tu es très observateur !");
                        return null;
                    }
                );
            case 3:
                return new Puzzle(
                    "Je peux voler sans ailes, pleurer sans yeux. Qui suis-je ?",
                    "nuage",
                    "Regarde le ciel...",
                    player -> {
                        System.out.println("Génial ! Tu as la tête dans les nuages !");
                        return null;
                    }
                );
            case 4:
                return new Puzzle(
                    "Je suis plus grand que la montagne, plus petit qu'un grain de sable. Qui suis-je ?",
                    "rien",
                    "Pense à l'absence de quelque chose...",
                    player -> {
                        System.out.println("Parfait ! Tu as trouvé la réponse !");
                        return null;
                    }
                );
            case 5:
                return new Puzzle(
                    "Je suis toujours dans le passé, je peux être créé dans le présent, mais le futur ne m'aura jamais. Qui suis-je ?",
                    "souvenir",
                    "C'est quelque chose qui reste dans ta mémoire...",
                    player -> {
                        System.out.println("Magnifique ! Tu as une excellente mémoire !");
                        return null;
                    }
                );
            case 6:
                return new Puzzle(
                    "Si 3 astronautes peuvent construire une base en 6 jours, combien de temps faudrait-il à 6 astronautes ?",
                    "3",
                    "Ce n'est pas une simple division...",
                    player -> {
                        System.out.println("Excellent calcul ! Tu es un vrai mathématicien spatial !");
                        return null;
                    }
                );
            case 7:
                return new Puzzle(
                    "Quelle est la prochaine lettre dans cette séquence : E, F, M, A, M, J, J, A, S, O, N, ?",
                    "d",
                    "Pense aux mois de l'année...",
                    player -> {
                        System.out.println("Brillant ! Tu as une excellente logique !");
                        return null;
                    }
                );
            case 8:
                return new Puzzle(
                    "Un astronaute pèse 80 kg sur Terre. Combien pèse-t-il sur la Lune ? (arrondi au kg près)",
                    "13",
                    "La gravité lunaire est environ 6 fois plus faible que celle de la Terre...",
                    player -> {
                        System.out.println("Impressionnant ! Tu connais bien la physique spatiale !");
                        return null;
                    }
                );
            case 9:
                return new Puzzle(
                    "Si tu es dans un vaisseau spatial qui tourne sur lui-même, quelle est la seule direction qui ne change jamais ?",
                    "haut",
                    "Pense à l'axe de rotation...",
                    player -> {
                        System.out.println("Génial ! Tu as une excellente compréhension de l'espace !");
                        return null;
                    }
                );
            default:
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean checkAnswer(String playerAnswer) {
        if (playerAnswer.equalsIgnoreCase(answer)) {
            solved = true;
            if (onSolve != null) {
                onSolve.apply(null);
            }
            return true;
        }
        return false;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
} 