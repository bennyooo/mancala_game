package com.example.mancala_game;

import java.util.List;

public class Game {
    // has two game areas and a start and stop method
    private List<Player> players;

    // TODO: get holes from players gameAreas and create game logic list to operate on

    private int numberOfPlayers;

    private String gameId;

    public Game(String gameId, int numberOfPlayers, int numberOfHoles, int numberOfStonesPerHole) {
        this.gameId = gameId + " was returned!";
        this.numberOfPlayers = numberOfPlayers;

        // create Player object and their gameAreas for the amount of players
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
