package com.example.mancala_game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTest {

    Game activeGame;
    String gameId = "testGameId123";
    List<String> playerNames = new ArrayList<>(Arrays.asList("Player1", "Player2"));
    int numberOfRegularHoles = 6;
    int numberOfStonesPerRegularHole = 3;

    @BeforeEach
    public void init(){
        activeGame = new Game(gameId, playerNames, numberOfRegularHoles, numberOfStonesPerRegularHole);
        activeGame.initializeGameLogicStructures();
    }

    /**
     * If we send the wrong playerName, the game shouldn't change i.e. the roundsPlayed should be the same
     */
    @Test
    public void givenWrongPlayer_whenMovingStone_thenCheckIfRoundsNotIncreasing(){

        activeGame.startGame();
        String actualActivePlayerName = activeGame.getActivePlayer().getPlayerName();

        // get a playerName other than the active player's name
        String otherPlayerName = playerNames.stream().filter(x -> !x.equals(actualActivePlayerName))
                .findFirst()
                .orElse(null);

        MoveAction moveAction = new MoveAction(0, otherPlayerName);
        int roundBeforeMove = activeGame.getRoundsPlayed();
        activeGame.performGameAction(moveAction);
        int roundAfterMove = activeGame.getRoundsPlayed();

        Assertions.assertEquals(roundBeforeMove, roundAfterMove);
    }

    /**
     * If a player selects a number outside of 1-6 the game should continue and let the player decide again.
     */
    @Test
    public void givenOutOfBoundsPosition_whenMovingStones_thenCheckIfPlayerStillActive(){
        activeGame.startGame();

        // get min and max of position, subtract/add 1 and send moveAction

    }

    // Implement game hint to see messages in client (wrong position etc.)
    // Test if player can go again, test if player can steal stones, test if chosen field has stones, otherwise go again.
}
