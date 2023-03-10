package com.example.mancala_game;

import java.util.*;

public class Game {
    // has two game areas and a start and stop method
    private GameState gameState;

    private Player activePlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private Deque<Player> playerGameOrder = new ArrayDeque<>();
    private ArrayList<Hole> gameLogicList = new ArrayList<>();
    private Deque<Hole> gameLogicDeque = new ArrayDeque<>();
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

        this.playerGameOrder.addAll(this.players);
    }

    public ArrayList<Hole> getGameLogicList() {
        return this.gameLogicList;
    }

    public void initializeGameLogicList(){
        int gameLogicCounter = 0;

        for (Player player : players){
            int playerPerspectiveCounter = 1;

            for(RegularHole hole : player.getGameArea().getRegularHoles()){
                hole.setPositionInGameLogic(gameLogicCounter);
                hole.setPositionFromPlayerPerspective(playerPerspectiveCounter);
                this.gameLogicList.add(hole);
                this.gameLogicDeque.addLast(hole);

                gameLogicCounter++;
                playerPerspectiveCounter++;
            }

            player.getGameArea().getMancalaHole().setPositionInGameLogic(gameLogicCounter);
            player.getGameArea().getMancalaHole().setPositionFromPlayerPerspective(playerPerspectiveCounter);
            this.gameLogicList.add(player.getGameArea().getMancalaHole());
            this.gameLogicDeque.addLast(player.getGameArea().getMancalaHole());

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
        return this.players;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
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
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Player getPlayerByPlayerName(String playerName){
        for(Player player : this.getPlayers()){
            if (player.getPlayerName().equals(playerName)){
                return player;
            }
        }
        return null;
    }

    public void fillFollowingHolesInGameLogic(int gameLogicStartPosition){
        int stonesToDistribute = 0;

        // loop over all holes and check if gameLogicStartPosition is correct, otherwise add hole to end
        for (Hole hole : gameLogicDeque){
            if (hole.getPositionInGameLogic() == gameLogicStartPosition){
                Hole holeToTakeStonesFrom = this.gameLogicDeque.removeFirst();
                stonesToDistribute = holeToTakeStonesFrom.takeAllStonesFromHole();
                this.gameLogicDeque.addLast(holeToTakeStonesFrom);
                break;
            }
            else {
                this.gameLogicDeque.addLast(this.gameLogicDeque.removeFirst());
            }
        }

        for (int j = 1; j <= stonesToDistribute; j++){
            Hole activeHole = this.gameLogicDeque.removeFirst();
            if (activeHole instanceof RegularHole || activeHole.equals(this.activePlayer.getGameArea().getMancalaHole())) {
                activeHole.addStoneToHole();
            }

            this.gameLogicDeque.addLast(activeHole);

            // if the last hole is a mancala hole, the currently active player can go again
            if (j == stonesToDistribute && activeHole.equals(this.activePlayer.getGameArea().getMancalaHole())){
                this.playerGameOrder.addLast(this.playerGameOrder.removeFirst());
            }
        }

    }

    public Deque<Player> getPlayerGameOrder() {
        return this.playerGameOrder;
    }

    public void setPlayerGameOrder(Deque<Player> playerGameOrder) {
        this.playerGameOrder = playerGameOrder;
    }

    public int getAllStonesInRegularHoles(Player player){
        int allStones = 0;
        for (RegularHole hole : player.getGameArea().getRegularHoles()){
            allStones += hole.getStonesInHole();
        }
        return allStones;
    }

    public boolean checkIfActiveGameAreaIsEmpty(Player player){
        return getAllStonesInRegularHoles(player) == 0;
    }

    public Deque<Hole> getGameLogicDeque() {
        return gameLogicDeque;
    }

    public void setGameLogicDeque(Deque<Hole> gameLogicDeque) {
        this.gameLogicDeque = gameLogicDeque;
    }
}
