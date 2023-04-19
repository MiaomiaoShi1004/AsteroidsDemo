package com.example.demo;

import java.io.*;
import java.util.*;

public class HighScore {
    private static final String HIGH_SCORE_FILE = "high_scores.txt";
    private List<PlayerScore> highScores;

    public HighScore() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public void loadHighScores() {
        // Load high scores from file
        try (BufferedReader br = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String playerName = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScores.add(new PlayerScore(playerName, score));
            }
        } catch (IOException e) {
        	System.out.printf("Error writing to high score file", e);
        }

        // Sort the high scores list
        Collections.sort(highScores);
    }

    public void updateHighScores(String playerName, int score) {
        // Check if the score is in the top 5
        if (highScores.size() < 5 || score > highScores.get(highScores.size() - 1).getScore()) {
            highScores.add(new PlayerScore(playerName, score));
            Collections.sort(highScores);

            // Keep only the top 5 scores
            while (highScores.size() > 5) {
                highScores.remove(highScores.size() - 1);
            }

            // Save the updated high scores to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
                for (PlayerScore entry : highScores) {
                    bw.write(entry.getPlayerName() + " " + entry.getScore() + "\n");
                }
            } catch (IOException e) {
            	System.out.printf("Error writing to high score file", e);
            }
        }
    }

    public List<PlayerScore> getHighScores() {
        return highScores;
    }
}
