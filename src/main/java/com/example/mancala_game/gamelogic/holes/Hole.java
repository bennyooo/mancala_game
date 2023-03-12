package com.example.mancala_game.gamelogic.holes;

public class Hole {
    private int stonesInHole;
    private int positionInGameLogic;
    private int positionFromPlayerPerspective;
    private boolean isMancalaHole;

    public Hole(int stonesInHole){
        this.stonesInHole = stonesInHole;
    }

    public int getStonesInHole() {
        return this.stonesInHole;
    }

    public void setStonesInHole(int stonesInHole) {
        this.stonesInHole = stonesInHole;
    }

    public void addStoneToHole(){
        this.stonesInHole++;
    }

    public int getPositionInGameLogic() {
        return this.positionInGameLogic;
    }

    public void setPositionInGameLogic(int positionInGameLogic) {
        this.positionInGameLogic = positionInGameLogic;
    }

    public int getPositionFromPlayerPerspective() {
        return this.positionFromPlayerPerspective;
    }

    public void setPositionFromPlayerPerspective(int positionFromPlayerPerspective) {
        this.positionFromPlayerPerspective = positionFromPlayerPerspective;
    }

    public boolean getIsMancalaHole() {
        return this.isMancalaHole;
    }

    public void setIsMancalaHole(boolean mancalaHole) {
        this.isMancalaHole = mancalaHole;
    }

    public int takeAllStonesFromHole(){
        int takenStones = this.stonesInHole;
        this.stonesInHole = 0;

        return takenStones;
    }
}
