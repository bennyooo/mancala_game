package com.example.mancala_game.gamelogic;

public class RegularHole extends Hole{

    public RegularHole(int stonesInHole) {
        super(stonesInHole);
        super.setIsMancalaHole(false);
    }
}
