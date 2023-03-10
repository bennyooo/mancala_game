package com.example.mancala_game;

public class MoveAction extends GameAction{

    private int startPosition;

    // the player who triggered the action
    private String activePlayerName;

    public MoveAction(int startPosition, String activePlayerName) {
        this.startPosition = startPosition;
        this.activePlayerName = activePlayerName;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public String getActivePlayerName() {
        return activePlayerName;
    }

    public void setActivePlayerName(String activePlayerName) {
        this.activePlayerName = activePlayerName;
    }
}
