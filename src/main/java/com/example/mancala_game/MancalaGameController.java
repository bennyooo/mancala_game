package com.example.mancala_game;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.*;

/**
 * Controls all interactions with the game API.
 */
@RestController
public class MancalaGameController {

    private Game myGame;
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @CrossOrigin
    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game myGame) {
        this.myGame = myGame;

        this.myGame.initializeGameLogicStructures();
        this.myGame.setGameState(GameState.CREATED);

        return this.myGame;
    }


    @CrossOrigin
    @GetMapping("/startGame")
    public Game startGame(){
        if (this.myGame.getGameState() != GameState.CREATED){
            throw new NullPointerException("Game not created!");
        }
        else {
            this.myGame.startGame();
            return this.myGame;
        }
    }

    @CrossOrigin
    @PostMapping("/moveStones")
    public Game moveStones(@RequestBody MoveAction moveAction) {
        return this.myGame.performGameAction(moveAction);
    }

    //TODO:
    // implement tests:
    // game needs to be created and started before first move.
    // startPosition stones cant be 0.
    // startPosition of hole can't be >6 or <1.
    // gamelogicposition has to be unique.
    // total number of stones should not be higher than stones times regular holes
    // after player1's turn his opponents m-hole should have the same stones
    // test APIs:
    // startGame should yield a game that is started
    // createGame should yield a game with at least two players
    // moveStones should yield a game where the chosen hole is empty

    public Game getMyGame() {
        return this.myGame;
    }

    public void setMyGame(Game myGame) {
        this.myGame = myGame;
    }
}
