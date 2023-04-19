package com.example.demo;

public class PlayerScore implements Comparable<PlayerScore> {
    private String playerName;
    private int score;

    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(PlayerScore o) {
        return Integer.compare(o.getScore(), this.score);
    }
}
