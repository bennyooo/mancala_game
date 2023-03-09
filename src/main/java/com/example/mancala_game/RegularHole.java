package com.example.mancala_game;

public class RegularHole extends Hole{

    // i.e. hole at first position from my perspective is checked against opponents last hole from his perspective

    public RegularHole(int stonesInHole) {
        super(stonesInHole);
        super.setMancalaHole(false);
    }

}
