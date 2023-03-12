package com.example.mancala_game;

import com.example.mancala_game.gamelogic.Game;
import com.example.mancala_game.gamelogic.MoveAction;
import com.example.mancala_game.gamelogic.Player;
import com.example.mancala_game.gamelogic.RegularHole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GameTest {

    Game activeGame;
    List<String> playerNames;

    @BeforeEach
    public void init(){
        String gameId = "testGameId123";
        List<String> playerNames = new ArrayList<>(Arrays.asList("Player1", "Player2"));
        int numberOfRegularHoles = 6;
        int numberOfStonesPerRegularHole = 3;

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

        MoveAction moveAction = new MoveAction(1, otherPlayerName);
        int roundBeforeMove = activeGame.getRoundsPlayed();
        activeGame.performGameAction(moveAction);
        int roundAfterMove = activeGame.getRoundsPlayed();

        Assertions.assertEquals(roundBeforeMove, roundAfterMove);
    }

    /**
     * If a player selects a number outside 1-6 the game should continue and let the player decide again.
     */
    @Test
    public void givenOutOfBoundsPosition_whenMovingStones_thenCheckIfSamePlayerIsStillActive(){
        activeGame.startGame();

        int maxPosition = Objects.requireNonNull(activeGame.getActivePlayer().getGameArea().getRegularHoles().stream()
                        .max(Comparator.comparing(RegularHole::getPositionFromPlayerPerspective))
                        .orElse(null))
                        .getPositionFromPlayerPerspective();

        int minPosition = Objects.requireNonNull(activeGame.getActivePlayer().getGameArea().getRegularHoles().stream()
                        .min(Comparator.comparing(RegularHole::getPositionFromPlayerPerspective))
                        .orElse(null))
                        .getPositionFromPlayerPerspective();

        MoveAction moveActionMin = new MoveAction(minPosition-1, activeGame.getActivePlayer().getPlayerName());
        MoveAction moveActionMax = new MoveAction(maxPosition+1, activeGame.getActivePlayer().getPlayerName());

        String activePlayerBeforeMove = activeGame.getActivePlayer().getPlayerName();

        activeGame.performGameAction(moveActionMin);
        activeGame.performGameAction(moveActionMax);

        String activePlayerAfterMove = activeGame.getActivePlayer().getPlayerName();

        Assertions.assertEquals(activePlayerBeforeMove, activePlayerAfterMove);
    }

    /**
     * We test whether a player can change the stone number in their opponents mancalaHole
     */
    @Test
    public void givenSetOfMovements_whenMovingStones_thenCheckIfOpponentsMancalaHoleDidNotChange(){
        activeGame.startGame();
        boolean opponentsMancalaHoleHasChanged = false;

        for (int i = 1; i <= 6; i++){

            for (Player player : activeGame.getPlayers()){
                int stonesInMancalaHoleBeforeMoveSet = activeGame.getPlayerByPlayerName(activeGame.getActivePlayer()
                                .getOppositePlayerName())
                        .getGameArea()
                        .getMancalaHole()
                        .getStonesInHole();

                MoveAction moveAction = new MoveAction(i, activeGame.getActivePlayer().getPlayerName());
                activeGame.performGameAction(moveAction);

                // the active player is already set for the next round so they are the "opposite" player from before.
                int stonesInMancalaHoleAfterMoveSet = activeGame.getActivePlayer()
                        .getGameArea()
                        .getMancalaHole()
                        .getStonesInHole();

                if (stonesInMancalaHoleBeforeMoveSet != stonesInMancalaHoleAfterMoveSet){
                    opponentsMancalaHoleHasChanged = true;
                }
            }
        }
        Assertions.assertFalse(opponentsMancalaHoleHasChanged);
    }

    /**
     * Test if the first active player can go again if they select the appropriate hole first.
     */
    @Test
    public void givenPositionToGoAgain_whenMovingStones_thenCheckIfSamePlayerIsStillActive(){
        activeGame.startGame();

        MoveAction moveAction = createMoveActionSoSamePlayerCanGoAgain();

        String activePlayerBeforeMove = activeGame.getActivePlayer().getPlayerName();
        activeGame.performGameAction(moveAction);
        String activePlayerAfterMove = activeGame.getActivePlayer().getPlayerName();

        Assertions.assertEquals(activePlayerBeforeMove, activePlayerAfterMove);
    }

    /**
     * If the player selects an empty hole they can go again.
     */
    @Test
    public void givenPositionWithZeroStones_whenMovingStones_thenCheckIfSamePlayerIsStillActive(){
        activeGame.startGame();

        MoveAction firstMoveAction = createMoveActionSoSamePlayerCanGoAgain();

        String activePlayerBeforeMove = activeGame.getActivePlayer().getPlayerName();
        activeGame.performGameAction(firstMoveAction);

        MoveAction secondMoveAction = new MoveAction(firstMoveAction.getStartPosition(), firstMoveAction.getActivePlayerName());
        activeGame.performGameAction(secondMoveAction);

        String activePlayerAfterTwoMoves = activeGame.getActivePlayer().getPlayerName();

        Assertions.assertEquals(activePlayerBeforeMove, activePlayerAfterTwoMoves);
    }

    /**
     * Testing whether stealing increases the amount of the respective mancalaHole by the respective amount
     */
    @Test
    public void givenPositionToGoAgainAndThenStealStones_whenMovingStones_thenCheckIfMancalaHoleStonesIncreased(){
        activeGame.startGame();

        MoveAction firstMoveAction = createMoveActionSoSamePlayerCanGoAgain();
        activeGame.performGameAction(firstMoveAction);

        int mHoleStoneCountBeforeStealing = activeGame.getActivePlayer().getGameArea().getMancalaHole().getStonesInHole();
        MoveAction stealStonesMoveAction = new MoveAction(1, firstMoveAction.getActivePlayerName());
        activeGame.performGameAction(stealStonesMoveAction);
        int mHoleStoneCountAfterStealing = activeGame.getPlayerByPlayerName(activeGame.getActivePlayer().getOppositePlayerName())
                .getGameArea()
                .getMancalaHole()
                .getStonesInHole();

        Assertions.assertEquals((mHoleStoneCountBeforeStealing + activeGame.getNumberOfStonesPerRegularHole()+1), mHoleStoneCountAfterStealing);
    }

    public MoveAction createMoveActionSoSamePlayerCanGoAgain(){
        // to get the correct position to go again at the start, our startingPosition's gameLogicPosition should be
        // "stonesInHole" away from mancalaHole's logicalPosition
        int mHoleGameLogicPosition = activeGame.getActivePlayer().getGameArea().getMancalaHole().getPositionInGameLogic();

        int startPosition = activeGame.getActivePlayer().getGameArea()
                .getHoleFromGameLogicPosition(mHoleGameLogicPosition - activeGame.getNumberOfStonesPerRegularHole())
                .getPositionFromPlayerPerspective();

        return new MoveAction(startPosition, activeGame.getActivePlayer().getPlayerName());
    }
}
