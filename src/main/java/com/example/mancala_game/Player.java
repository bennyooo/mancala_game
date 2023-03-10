package com.example.mancala_game;

public class Player {
    // has a score and a game area
    private int playerScore;
    private String playerName;
    private String oppositePlayerName;
    private GameArea gameArea;

    public Player(String playerName, String oppositePlayerName, GameArea gameArea){
        this.playerName = playerName;
        this.oppositePlayerName = oppositePlayerName;
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

    public String getOppositePlayerName() {
        return oppositePlayerName;
    }

    public void setOppositePlayerName(String oppositePlayerName) {
        this.oppositePlayerName = oppositePlayerName;
    }
}
