package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HighScoreFileIO {

    private static final String HIGH_SCORE_FILE = "highscore.dat";

    public static int loadHighScore() {
        Path path = Path.of(HIGH_SCORE_FILE);
        if (!Files.exists(path)) {
            return 0;
        }
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return 0;
            }
            String cleaned = content.replaceAll("[^0-9-]", "");
            if (cleaned.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(cleaned);
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public static int saveIfHighScore(int score) {
        int currentBest = loadHighScore();
        if (score > currentBest) {
            writeHighScore(score);
            return score;
        }
        return currentBest;
    }

    private static void writeHighScore(int score) {
        try {
            Files.writeString(Path.of(HIGH_SCORE_FILE), String.valueOf(score), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore write errors to avoid crashing the game
        }
    }
}