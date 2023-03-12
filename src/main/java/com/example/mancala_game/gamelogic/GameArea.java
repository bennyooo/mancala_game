package com.example.mancala_game.gamelogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * The game area of the player on the board.
 */
public class GameArea {
    // has Holes. Needs methods to initialize holes.

    // MancalaHole can be in first or last position
    // the game logic list will consist of two concatenated lists (?)
    private AreaOrientation areaOrientation;
    private List<RegularHole> regularHoles = new ArrayList<>();
    private MancalaHole mancalaHole;
    private int numberOfRegularHoles;
    private int numberOfStonesPerRegularHole;

    @JsonCreator
    public GameArea(@JsonProperty("areaOrientation") AreaOrientation areaOrientation, @JsonProperty("numberOfRegularHoles") int numberOfRegularHoles,@JsonProperty("numberOfStonesPerRegularHole") int numberOfStonesPerRegularHole) {
        this.areaOrientation = areaOrientation;

        this.numberOfRegularHoles = numberOfRegularHoles;
        this.numberOfStonesPerRegularHole = numberOfStonesPerRegularHole;

        for(int i = 0; i < this.numberOfRegularHoles; i++){
            this.regularHoles.add(new RegularHole(numberOfStonesPerRegularHole));
        }
        this.mancalaHole = new MancalaHole(0);
    }

    public AreaOrientation getAreaOrientation() {
        return areaOrientation;
    }

    public void setAreaOrientation(AreaOrientation areaOrientation) {
        this.areaOrientation = areaOrientation;
    }

    public int getNumberOfRegularHoles() {
        return numberOfRegularHoles;
    }

    public void setNumberOfRegularHoles(int numberOfRegularHoles) {
        this.numberOfRegularHoles = numberOfRegularHoles;
    }

    public int getNumberOfStonesPerRegularHole() {
        return numberOfStonesPerRegularHole;
    }

    public void setNumberOfStonesPerRegularHole(int numberOfStonesPerRegularHole) {
        this.numberOfStonesPerRegularHole = numberOfStonesPerRegularHole;
    }

    public List<RegularHole> getRegularHoles() {
        return this.regularHoles;
    }

    public void setRegularHoles(List<RegularHole> regularHoles) {
        this.regularHoles = regularHoles;
    }

    public MancalaHole getMancalaHole() {
        return mancalaHole;
    }

    public void setMancalaHole(MancalaHole mancalaHole) {
        this.mancalaHole = mancalaHole;
    }

    public int getGameLogicPositionFromPlayerPerspectivePosition(int positionFromPlayerPerspective){
        for(RegularHole regularHole : regularHoles){
            if (regularHole.getPositionFromPlayerPerspective() == positionFromPlayerPerspective){
                return regularHole.getPositionInGameLogic();
            }
        }
        return 0;
    }

    public Hole getHoleFromGameLogicPosition(int gameLogicPosition){
        for(RegularHole regularHole : regularHoles){
            if (regularHole.getPositionInGameLogic() == gameLogicPosition){
                return regularHole;
            }
        }
        return null;
    }

    public Hole getHoleFromPlayerPerspectivePosition(int playerPerspectivePosition){
        for(RegularHole regularHole : regularHoles){
            if (regularHole.getPositionFromPlayerPerspective() == playerPerspectivePosition){
                return regularHole;
            }
        }
        return null;
    }
}
