package com.example.mancala_game;

import com.example.mancala_game.gamelogic.*;
import com.example.mancala_game.gamelogic.holes.Hole;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Simple frontend that calls the game API and renders the board in a console application.
 */
public class MancalaGameFrontEnd {

    public static void main(String args[]){
        boolean gameRunning = false;
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> playerList = new ArrayList<>();
        playerList.add("player1");
        playerList.add("player2");

        String createGameResponse = callCreateGame("Test123", playerList, 6, 3);
        assert gson.fromJson(createGameResponse, Game.class).getGameState().equals(GameState.CREATED);

        String startGameResponse = callStartGame();

        Game game = gson.fromJson(startGameResponse, Game.class);

        if (game.getGameState() == GameState.STARTED){
            gameRunning = true;
        }

        GameArea[] gameAreasToRender = new GameArea[game.getNumberOfPlayers()];

        while (gameRunning) {
            if (game.getGameState() != GameState.ENDED) {
                System.out.println("\n");
                System.out.println("========================================");
                System.out.println("Running game: " + game.getGameId() + " round " + game.getRoundsPlayed());

                System.out.println("Players: " + game.getPlayers().get(0).getPlayerName() + " " + game.getPlayers().get(1).getPlayerName());
                String activePlayerName = game.getActivePlayer().getPlayerName();
                System.out.println("========================================");
                System.out.println("\n");

                System.out.println(activePlayerName + "'s turn \n");

                for (Player player : game.getPlayers()) {
                    if (player.getGameArea().getAreaOrientation() == AreaOrientation.UPSIDE_DOWN) {
                        gameAreasToRender[0] = player.getGameArea();
                    } else {
                        gameAreasToRender[1] = player.getGameArea();
                    }
                }

                for (GameArea gameArea : gameAreasToRender) {
                    String stringToRender = renderGameAreaForConsole(gameArea);
                    System.out.println(stringToRender);
                }

                if (game.getActivePlayer().getGameArea().getAreaOrientation() == AreaOrientation.REGULAR) {
                    System.out.print("\n Select position of hole (1-6):");
                }
                else {
                    System.out.print("\n Select position of hole (6-1):");
                }

                try {
                    int selectedPosition = Integer.parseInt(reader.readLine());
                    game = gson.fromJson(callMoveStones(selectedPosition, activePlayerName), Game.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            else {
                gameRunning = false;
            }
        }
        String playerNameWhoWon = game.getPlayerNameWhoWon();
        System.out.println("Game finished, " + playerNameWhoWon + " won with " + game.getPlayerByPlayerName(playerNameWhoWon).getPlayerScore() + " points!");
    }

    public static String renderGameAreaForConsole(GameArea gameArea){
        StringBuilder stringToRender = new StringBuilder();
        ArrayList<Hole> holesToDisplay = new ArrayList<>();

        if (gameArea.getAreaOrientation() == AreaOrientation.REGULAR){
            // TODO: instead of repeat 1 create number of " " characters dynamically based on the stones in the opponents mancala hole
            // we create some space for the regular game so that they both align in the terminal
            stringToRender.append("  ").append(" ".repeat(1)).append("  ");

            holesToDisplay.addAll(gameArea.getRegularHoles());
            holesToDisplay.add(gameArea.getMancalaHole());
        }
        else {
            holesToDisplay.add(gameArea.getMancalaHole());
            gameArea.getRegularHoles().sort(Comparator.comparing(Hole::getPositionInGameLogic).reversed());
            holesToDisplay.addAll(gameArea.getRegularHoles());
        }

        for (Hole hole : holesToDisplay){
            if (!hole.getIsMancalaHole()){
                stringToRender.append("|").append(hole.getStonesInHole()).append("|");
            }
            else {
                stringToRender.append("**").append(hole.getStonesInHole()).append("**");
            }
        }

        return stringToRender.toString();
    }

    public static String callCreateGame(String gameId, List<String> playerList, int numberOfRegularHoles, int numberOfStonesPerRegular){

        final String url = "http://localhost:8080/createGame";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        JSONObject gameCreationObject = new JSONObject();
        gameCreationObject
                .put("gameId", gameId)
                .put("playerNames", playerList)
                .put("numberOfRegularHoles", numberOfRegularHoles)
                .put("numberOfStonesPerRegularHole", numberOfStonesPerRegular);

        HttpEntity<String> entity = new HttpEntity<>(gameCreationObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    public static String callStartGame(){

        final String url = "http://localhost:8080/startGame";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

    public static String callMoveStones(int startPosition, String activePlayerName){

        final String url = "http://localhost:8080/moveStones";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        JSONObject moveStonesObject = new JSONObject();
        moveStonesObject
                .put("startPosition", startPosition)
                .put("activePlayerName", activePlayerName);

        HttpEntity<String> entity = new HttpEntity<String>(moveStonesObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }
}
