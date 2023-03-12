package com.example.mancala_game.gamelogic;

public class RegularHole extends Hole{

    // TODO: to get the opposite hole we use the position from the player perspective
    // if the player with regular orientation makes the move: opposite hole position = (position of mancala hole of opposite player) - (position of the hole where the last stone was put into)
    // then we get the positionInGameLogic from positionFromPlayerPerspective
    public RegularHole(int stonesInHole) {
        super(stonesInHole);
        super.setIsMancalaHole(false);
    }
}
