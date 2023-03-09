package com.example.mancala_game;

import org.springframework.web.bind.annotation.*;

@RestController
public class MancalaGameController {

    private Game myGame;

    @PostMapping("/createGame")
    public Game createGame(@RequestBody Game myGame) {
        // operations are done on this object
        this.myGame = myGame;

        this.myGame.initializeGameLogicList();

        return myGame;
    }


    //TODO: postmapping for selected position etc.
    @GetMapping("/startGame")
    public Game startGame(){
        this.myGame.setGameState(GameState.STARTED);

        String startingPlayerName = "player1";

        for(Player player : this.myGame.getPlayers()){
            if (player.getPlayerName().equals(startingPlayerName)){
                this.myGame.setActivePlayer(player);
            }
        }

        return this.myGame;
    }
}
