package com.example.mancala_game;

public class MancalaHole extends Hole{

    public MancalaHole(int stones) {
        super(stones);
        super.setMancalaHole(true);
    }
}
