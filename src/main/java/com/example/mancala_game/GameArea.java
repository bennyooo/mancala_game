package com.example.mancala_game;

import java.util.List;

public class GameArea {
    // has Holes. Needs methods to initialize holes.

    // MancalaHole can be in first or last position
    // the game logic list will consist of two concatenated lists (?)
    private List<Hole> holes;
    private int numberOfHoles;
    private int numberOfStonesPerHole;

    public GameArea(int numberOfHoles, int numberOfStonesPerHole) {
        this.numberOfHoles = numberOfHoles;
        this.numberOfStonesPerHole = numberOfStonesPerHole;

        // TODO: create holes and fill with stones accordingly
    }
}
