package com.example.mancala_game;

import org.springframework.web.bind.annotation.*;

@RestController
public class MancalaGameController {

    private Game myGame;

    @CrossOrigin
    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game myGame) {
        // operations are done on this object
        this.myGame = myGame;

        this.myGame.initializeGameLogicList();

        return this.myGame;
    }


    @CrossOrigin
    @GetMapping("/startGame")
    public Game startGame(){
        try {
            this.myGame.setGameState(GameState.STARTED);
        }
        catch (NullPointerException ex){
            System.out.println("Game not started");
            System.out.println(ex.getMessage());
        }

        // get first player from dequeue and add him again at the end
        this.myGame.setActivePlayer(this.myGame.getPlayerGameOrder().removeFirst());
        this.myGame.getPlayerGameOrder().addLast(this.myGame.getActivePlayer());

        return this.myGame;
    }

    @CrossOrigin
    @PostMapping("/moveStones")
    public Game moveStones(@RequestBody MoveAction moveAction) {
        int startingPosition = moveAction.getStartPosition();

        // check if the sent command came from the active player
        if (!moveAction.getActivePlayerName().equals(this.myGame.getActivePlayer().getPlayerName())){
            throw new Error("Player in action doesn't match active player");
        }

        // get gameLogicPosition from Players gameArea
        int gameLogicStartPosition = this.myGame.getActivePlayer().getGameArea().getGameLogicPositionFromPlayerPerspectivePosition(startingPosition);

        //int allStonesInHole = this.myGame.getActivePlayer().getGameArea().getHoleFromGameLogicPosition(gameLogicStartPosition).takeAllStonesFromHole();

        this.myGame.fillFollowingHolesInGameLogic(gameLogicStartPosition);

        // set active player for next round
        this.myGame.setActivePlayer(this.myGame.getPlayerGameOrder().removeFirst());
        this.myGame.getPlayerGameOrder().addLast(this.myGame.getActivePlayer());

        // update scores and check if game area empty
        for (Player player : this.myGame.getPlayers()){
            player.setPlayerScore(player.getGameArea().getMancalaHole().getStonesInHole());

            if (this.myGame.checkIfGameAreaIsEmpty(player)){
                this.myGame.setGameState(GameState.ENDED);
            }
        }

        // check if the number of stones in the game doesn't deviate from the initial number
        if (this.myGame.getAllStonesInGame() != this.myGame.getInitialNumberOfStonesInGame()){
            throw new RuntimeException("Mismatch of stones in game!");
        }

        return this.myGame;
    }

    //TODO:
    // implement tests:
    // game needs to be created and started before first move.
    // startPosition stones cant be 0.
    // startPosition of hole can't be >6 or <1.
    // gamelogicposition has to be unique.
    // total number of stones should not be higher than stones times regular holes
    // after player1's turn his opponents m-hole should have the same stones

    public Game getMyGame() {
        return this.myGame;
    }

    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }
}
