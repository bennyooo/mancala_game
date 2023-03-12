package com.example.mancala_game.gamelogic;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This contains all the game logic.
 */
public class Game {
    private final transient Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private GameState gameState;
    private Player activePlayer;
    private String playerNameWhoWon = "";
    private ArrayList<Player> players = new ArrayList<>();
    private Deque<Player> playerGameOrder = new ArrayDeque<>();
    private ArrayList<Hole> gameLogicList = new ArrayList<>();
    private Deque<Hole> gameLogicDeque = new ArrayDeque<>();
    private int roundsPlayed;
    private int numberOfPlayers;
    private int numberOfStonesPerRegularHole;

    private int initialNumberOfStonesInGame;
    private int numberOfRegularHoles;
    private String gameId;

    public Game(String gameId, List<String> playerNames, int numberOfRegularHoles, int numberOfStonesPerRegularHole) {
        this.gameId = gameId;
        this.numberOfPlayers = playerNames.size();
        this.numberOfRegularHoles = numberOfRegularHoles;
        this.numberOfStonesPerRegularHole = numberOfStonesPerRegularHole;
        this.roundsPlayed = 0;
        this.initialNumberOfStonesInGame = numberOfRegularHoles * numberOfStonesPerRegularHole * playerNames.size();

        int i = 1;
        for (String playerName : playerNames) {
            // find the opposite PlayerName from player list. We need this later for identifying the opposite hole
            String oppositePlayerName = playerNames.stream().filter(x -> !x.equals(playerName)).findFirst().orElse(null);

            if (i % 2 == 0) {
                this.players.add(new Player(playerName, oppositePlayerName, new GameArea(AreaOrientation.UPSIDE_DOWN, numberOfRegularHoles, numberOfStonesPerRegularHole)));
            }
            else {
                this.players.add(new Player(playerName, oppositePlayerName, new GameArea(AreaOrientation.REGULAR, numberOfRegularHoles, numberOfStonesPerRegularHole)));
            }
            i++;
        }

        this.playerGameOrder.addAll(this.players);
    }

    public ArrayList<Hole> getGameLogicList() {
        return this.gameLogicList;
    }

    public void setGameLogicList(ArrayList<Hole> gameLogicList) {
        this.gameLogicList = gameLogicList;
    }

    public void initializeGameLogicStructures(){
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

    public void startGame(){
        this.setGameState(GameState.STARTED);

        // get first player from dequeue and add them again at the end
        this.setActivePlayer(this.getPlayerGameOrder().removeFirst());
        this.getPlayerGameOrder().addLast(this.getActivePlayer());
    }

    public GameArea getGameAreaForPlayerName(String playerName){
        for (Player player : this.getPlayers()){
            if (player.getPlayerName().equals(playerName)){
                return player.getGameArea();
            }
        }
        return null;
    }

    public Game performGameAction(GameAction gameAction){

        // Possible other GameActions in the future so we need to check the type
        if(gameAction instanceof MoveAction moveAction){
            // check if the sent command came from the active player
            if (!moveAction.getActivePlayerName().equals(this.getActivePlayer().getPlayerName())){
                logger.log(Level.INFO, "Command not sent from active player. Try again with correct player.");
                return this;
            }
            else {

                int startingPosition = moveAction.getStartPosition();

                if(startingPosition > this.getActivePlayer().getGameArea().getNumberOfRegularHoles() || startingPosition < 1){
                    logger.log(Level.INFO, "Selected position out of bounds for this board. Select a different position.");
                    return this;
                }
                else if (activePlayer.getGameArea().getHoleFromPlayerPerspectivePosition(startingPosition).getStonesInHole() == 0){
                    logger.log(Level.INFO, "Selected position has no stones. Select a different position.");
                    return this;
                }

                else {
                    // get gameLogicPosition from Players gameArea
                    int gameLogicStartPosition = this.getActivePlayer().getGameArea().getGameLogicPositionFromPlayerPerspectivePosition(startingPosition);

                    this.fillFollowingHolesInGameLogic(gameLogicStartPosition);

                    // set active player for next round
                    this.setActivePlayer(this.getPlayerGameOrder().removeFirst());
                    this.getPlayerGameOrder().addLast(this.getActivePlayer());

                    // update scores and check if game areas are empty
                    for (Player player : this.getPlayers()) {
                        player.setPlayerScore(player.getGameArea().getMancalaHole().getStonesInHole());

                        if (this.checkIfGameAreaIsEmpty(player)) {
                            this.setGameState(GameState.ENDED);

                            this.setPlayerNameWhoWon(Objects.requireNonNull(this
                                            .getPlayers()
                                            .stream()
                                            .max(Comparator.comparing(Player::getPlayerScore))
                                            .orElse(null))
                                            .getPlayerName());
                        }
                    }
                }
            }
        }

        // After every gameAction check if the number of stones in the game doesn't deviate from the initial number
        if (this.getAllStonesInGame() != this.getInitialNumberOfStonesInGame()) {
            throw new RuntimeException("Mismatch of stones in game!");
        }

        this.setRoundsPlayed(this.getRoundsPlayed() + 1);
        return this;
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

        for (int s = 1; s <= stonesToDistribute; s++){
            Hole activeHole = this.gameLogicDeque.removeFirst();
            // only add stones to regular hole or the player's mancala hole
            if (activeHole instanceof RegularHole || activeHole.equals(this.activePlayer.getGameArea().getMancalaHole())) {
                activeHole.addStoneToHole();
            }

            this.gameLogicDeque.addLast(activeHole);

            // if the last hole is a mancala hole, the currently active player can go again
            if (s == stonesToDistribute){
                if (activeHole.equals(this.activePlayer.getGameArea().getMancalaHole())) {
                    this.playerGameOrder.addLast(this.playerGameOrder.removeFirst());
                }

                // last hole was regular hole that now has one stone -> rule of capturing stones from opposite field applies
                if (activeHole instanceof RegularHole && activeHole.getStonesInHole() == 1){
                    // we need to know on which side we are
                    int positionFromPlayerPerspectiveActiveHole = activeHole.getPositionFromPlayerPerspective();

                    int allStonesFromOppositeHole = 0;

                    // check if active hole belongs to opponents area, if so take own game area to remove stones
                    if (this.getPlayerByPlayerName(this.activePlayer.getOppositePlayerName()).getGameArea().getRegularHoles().contains(activeHole)){
                        allStonesFromOppositeHole = this.activePlayer.getGameArea()
                                .getHoleFromPlayerPerspectivePosition((this.numberOfRegularHoles+1)-positionFromPlayerPerspectiveActiveHole)
                                .takeAllStonesFromHole();
                    }
                    else {
                        allStonesFromOppositeHole = this.getPlayerByPlayerName(this.activePlayer.getOppositePlayerName())
                                .getGameArea().getHoleFromPlayerPerspectivePosition((this.numberOfRegularHoles+1)-positionFromPlayerPerspectiveActiveHole)
                                .takeAllStonesFromHole();
                    }

                    // only take stones if the opposite hole is not empty
                    if (allStonesFromOppositeHole > 0) {
                        int totalStones = allStonesFromOppositeHole + activeHole.takeAllStonesFromHole();
                        this.activePlayer.getGameArea().getMancalaHole().addMultipleStonesToMancalaHole(totalStones);
                    }
                }
            }
        }
    }

    public int getNumberOfStonesPerRegularHole() {
        return numberOfStonesPerRegularHole;
    }

    public void setNumberOfStonesPerRegularHole(int numberOfStonesPerRegularHole) {
        this.numberOfStonesPerRegularHole = numberOfStonesPerRegularHole;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
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

    public int getInitialNumberOfStonesInGame() {
        return this.initialNumberOfStonesInGame;
    }

    public void setInitialNumberOfStonesInGame(int initialNumberOfStonesInGame) {
        this.initialNumberOfStonesInGame = initialNumberOfStonesInGame;
    }

    public String getPlayerNameWhoWon() {
        return this.playerNameWhoWon;
    }

    public void setPlayerNameWhoWon(String playerNameWhoWon) {
        this.playerNameWhoWon = playerNameWhoWon;
    }

    public Deque<Player> getPlayerGameOrder() {
        return this.playerGameOrder;
    }

    public void setPlayerGameOrder(Deque<Player> playerGameOrder) {
        this.playerGameOrder = playerGameOrder;
    }

    public int getAllStonesInGame(){
        int allStonesInGame = 0;
        for (Player player : players){
            int stonesInRegularHoles = getAllStonesInRegularHoles(player);
            int stonesInMancalaHole = player.getGameArea().getMancalaHole().getStonesInHole();

            allStonesInGame += (stonesInRegularHoles + stonesInMancalaHole);
        }
        return allStonesInGame;
    }

    public int getAllStonesInRegularHoles(Player player){
        int allStonesInRegularHoles = 0;
        for (RegularHole hole : player.getGameArea().getRegularHoles()){
            allStonesInRegularHoles += hole.getStonesInHole();
        }
        return allStonesInRegularHoles;
    }

    public int getRoundsPlayed() {
        return this.roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public boolean checkIfGameAreaIsEmpty(Player player){
        return getAllStonesInRegularHoles(player) == 0;
    }

    public Deque<Hole> getGameLogicDeque() {
        return gameLogicDeque;
    }

    public void setGameLogicDeque(Deque<Hole> gameLogicDeque) {
        this.gameLogicDeque = gameLogicDeque;
    }
}
