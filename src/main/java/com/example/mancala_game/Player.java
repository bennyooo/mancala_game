package com.example.mancala_game;

public class Player {
    // has a score and a game area
    private int playerScore;
    private String playerName;
    private GameArea gameArea;

    public Player(String playerName, GameArea gameArea){
        this.playerName = playerName;
        this.gameArea = gameArea;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return this.playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameArea getGameArea() {
        return this.gameArea;
    }

    public void setGameArea(GameArea gameArea) {
        this.gameArea = gameArea;
    }
}
