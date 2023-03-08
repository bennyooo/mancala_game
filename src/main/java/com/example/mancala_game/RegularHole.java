package com.example.mancala_game;

public class RegularHole extends Hole{

    // i.e. hole at first position from my perspective is checked against opponents last hole from his perspective
    private int positionFromPlayerPerspective;
    private int positionFromOpponentsPerspective;
    private int positionInGameLogic;
    public RegularHole(int stonesInHole, int positionFromPlayerPerspective, int positionInGameLogic, int amountOfHolesPerPlayer) {
        super(stonesInHole);
        this.positionFromPlayerPerspective = positionFromPlayerPerspective;
        this.positionInGameLogic = positionInGameLogic;
        this.positionFromOpponentsPerspective = (amountOfHolesPerPlayer+1) - positionFromPlayerPerspective;
    }
}
