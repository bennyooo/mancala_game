package com.example.mancala_game.gamelogic;

/**
 * Parent class for actions that can be sent to the game
 */
public class GameAction {
    // the player who triggered the action
    private String activePlayerName;

    public GameAction(String activePlayerName) {
        this.activePlayerName = activePlayerName;
    }

    public String getActivePlayerName() {
        return activePlayerName;
    }

    public void setActivePlayerName(String activePlayerName) {
        this.activePlayerName = activePlayerName;
    }
}
