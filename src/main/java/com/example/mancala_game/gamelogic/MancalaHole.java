package com.example.mancala_game.gamelogic;

public class MancalaHole extends Hole{

    public MancalaHole(int stones) {
        super(stones);
        super.setIsMancalaHole(true);
    }

    public void addMultipleStonesToMancalaHole(int stones){
        super.setStonesInHole(super.getStonesInHole() + stones);
    }
}
