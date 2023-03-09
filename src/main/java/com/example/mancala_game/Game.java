package com.example.mancala_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Game {
    // has two game areas and a start and stop method
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Hole> gameLogicList = new ArrayList<>();
    // TODO: get holes from players gameAreas and create game logic list to operate on
    private int numberOfPlayers;
    private int numberOfRegularHoles;
    private String gameId;
    public Game(String gameId, String[] playerNames, int numberOfRegularHoles, int numberOfStonesPerRegularHole) {
        this.gameId = gameId;
        this.numberOfPlayers = playerNames.length;
        this.numberOfRegularHoles = numberOfRegularHoles;

        int i = 1;
        for (String playerName : playerNames) {
            if (i % 2 == 0) {
                this.players.add(new Player(playerName, new GameArea(AreaOrientation.REGULAR, numberOfRegularHoles, numberOfStonesPerRegularHole)));
            }
            else {
                this.players.add(new Player(playerName, new GameArea(AreaOrientation.UPSIDE_DOWN, numberOfRegularHoles, numberOfStonesPerRegularHole)));
            }
            i++;
        }
    }

    public ArrayList<Hole> getGameLogicList() {
        return gameLogicList;
    }

    public void initializeGameLogicList(){
        int gameLogicCounter = 1;

        for (Player player : players){
            int playerPerspectiveCounter = 1;

            for(RegularHole hole : player.getGameArea().getRegularHoles()){
                hole.setPositionInGameLogic(gameLogicCounter);
                hole.setPositionFromPlayerPerspective(playerPerspectiveCounter);
                this.gameLogicList.add(hole);

                gameLogicCounter++;
                playerPerspectiveCounter++;
            }

            player.getGameArea().getMancalaHole().setPositionInGameLogic(gameLogicCounter);
            player.getGameArea().getMancalaHole().setPositionFromPlayerPerspective(playerPerspectiveCounter);
            this.gameLogicList.add(player.getGameArea().getMancalaHole());

            gameLogicCounter++;
        }
    }

    public int getGameLogicPositionOfOpposingHole(int positionFromPlayerPerspectiveOrignalHole){
        
        return 0;
    }

    public void setGameLogicList(ArrayList<Hole> gameLogicList) {
        this.gameLogicList = gameLogicList;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setGameLogicList(){

    }
    public void setPositionForHoles(){

    }

    public int getNumberOfRegularHoles() {
        return this.numberOfRegularHoles;
    }

    public void setNumberOfRegularHoles(int numberOfRegularHoles) {
        this.numberOfRegularHoles = numberOfRegularHoles;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
