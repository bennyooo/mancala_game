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
    
    @Test
    public void givenWrongPlayer_whenMovingStone_thenCheckIfRoundsNotIncreasing(){

        activeGame.startGame();
        String actualActivePlayerName = activeGame.getActivePlayer().getPlayerName();
        String otherPlayerName = playerNames.stream().filter(x -> !x.equals(actualActivePlayerName)).findFirst().orElse(null);

        // if we send the wrong playerName, the game shouldn't change i.e. the roundsPlayed should be the same
        MoveAction moveAction = new MoveAction(0, otherPlayerName);
        int roundBeforeMove = activeGame.getRoundsPlayed();
        activeGame.performGameAction(moveAction);
        int roundAfterMove = activeGame.getRoundsPlayed();

        Assertions.assertEquals(roundBeforeMove, roundAfterMove);
    }
}
