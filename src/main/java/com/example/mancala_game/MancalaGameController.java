package com.example.mancala_game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MancalaGameController {

    private Game myGame;

    @GetMapping("/startGame")
    public Game greeting(@RequestParam(value="gameId", defaultValue="game123") String gameId) {
        //TODO: create params for the numbers
        Game myGame = new Game(gameId, 2, 6, 4);

        // operations are done on this object
        this.myGame = myGame;

        return myGame;
    }

    //TODO: postmapping for selected position etc.

}
