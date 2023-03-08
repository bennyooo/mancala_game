package com.example.mancala_game;

public class Hole {
    // has stones and methods to add, remove and delete them
    private int stonesInHole;

    public Hole(int stonesInHole){
        this.stonesInHole = stonesInHole;
    }

    public int getStonesInHole() {
        return stonesInHole;
    }

    public void setStonesInHole(int stonesInHole) {
        this.stonesInHole = stonesInHole;
    }
}
