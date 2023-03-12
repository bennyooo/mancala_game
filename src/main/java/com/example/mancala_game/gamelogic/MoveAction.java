package com.example.mancala_game.gamelogic;

public class MoveAction extends GameAction{
    private int startPosition;

    public MoveAction(int startPosition, String activePlayerName) {
        super(activePlayerName);
        this.startPosition = startPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
}
