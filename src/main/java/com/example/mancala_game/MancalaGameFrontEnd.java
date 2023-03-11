package com.example.mancala_game;

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

public class MancalaGameFrontEnd {


    public static void main(String args[]){
        boolean gameRunning = false;
        int numberOfRounds = 0;

        String createGameResponse = callCreateGame();

        String startGameResponse = callStartGame();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Gson gson = new Gson();
        Game game = gson.fromJson(startGameResponse, Game.class);

        if (game.getGameState() == GameState.STARTED){
            gameRunning = true;
        }

        GameArea[] gameAreasToRender = new GameArea[game.getNumberOfPlayers()];

        while (gameRunning) {
            numberOfRounds++;
            // TODO: we only need to render after every moveStones
            if (game.getGameState() != GameState.ENDED) {
                System.out.println("\n");
                System.out.println("========================================");
                System.out.println("Running game: " + game.getGameId() + " round " + numberOfRounds);

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

                // TODO: read input and update game object
                System.out.print("\n Select position of hole:");
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
        // TODO: print winner
        //System.out.println(game);
    }

    public static String renderGameAreaForConsole(GameArea gameArea){
        String stringToRender = "";
        ArrayList<Hole> holesToDisplay = new ArrayList<>();

        if (gameArea.getAreaOrientation() == AreaOrientation.REGULAR){
            stringToRender += "     ";
            holesToDisplay.addAll(gameArea.getRegularHoles());
            holesToDisplay.add(gameArea.getMancalaHole());
        }
        else {
            holesToDisplay.add(gameArea.getMancalaHole());
            // TODO: reverse order of regularHoles
            gameArea.getRegularHoles().sort(Comparator.comparing(Hole::getPositionInGameLogic).reversed());
            holesToDisplay.addAll(gameArea.getRegularHoles());
        }

        for (Hole hole : holesToDisplay){
            if (!hole.getIsMancalaHole()){
                stringToRender += "|" + hole.getStonesInHole() + "|";
            }
            else {
                stringToRender += "**" + hole.getStonesInHole() + "**";
            }
        }

        return stringToRender;
    }

    public static String callCreateGame(){

        final String url = "http://localhost:8080/createGame";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        List<String> playerList = new ArrayList<>();
        playerList.add("player1");
        playerList.add("player2");

        JSONObject gameCreationObject = new JSONObject();
        gameCreationObject
                .put("gameId", "test123")
                .put("playerNames", playerList)
                .put("numberOfRegularHoles", 6)
                .put("numberOfStonesPerRegularHole", 3);

        HttpEntity<String> entity = new HttpEntity<String>(gameCreationObject.toString(), headers);

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
