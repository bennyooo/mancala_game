package com.example.mancala_game;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MancalaGameController {
    @RequestMapping("/getGame")
    public Game greeting(@RequestParam(value="gameId", defaultValue="game123") String gameId) {
        return new Game(gameId);
    }
}
