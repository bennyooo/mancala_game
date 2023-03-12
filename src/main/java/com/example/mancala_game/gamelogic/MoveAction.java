package com.example.mancala_game.gamelogic;

/**
 * Action for moving the stones over the board.
 */
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
