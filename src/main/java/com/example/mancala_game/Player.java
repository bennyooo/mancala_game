package com.example.mancala_game;

public class Player {
    // has a score and a game area
    private int playerScore;
    private GameArea gameArea;

    //TODO: necessary?
    private GameArea oppositeGameArea;

    public Player(GameArea gameArea){
        this.gameArea = gameArea;
    }
}
