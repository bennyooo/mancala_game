package com.example.mancala_game;

public class Game {

    private String gameId;

    public Game(String gameId) {
        this.gameId = gameId + " was returned!";
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
