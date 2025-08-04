package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.messages.toClient.*;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.messages.toClient.StampStartTurnMessage;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Block;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


/**
 *The LobbyController class represents a controller for managing a lobby in a game.
 *@author Andrea Gollo, Gabriele Marra, Francesco Foresti, Marco Mariotti
 */
public class LobbyController implements Runnable, Serializable {
    private final int numberOfPlayers;
    private final String lobbyName;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private final ArrayList<String> nicknameList = new ArrayList<>();
    private Match match;
    private boolean start;
    private final int numberOfCommonGoals;
    private TurnController activeTurnController;
    private final Object lock;

    /**
     *Constructs a LobbyController object with the specified lobby name, number of players, and number of common goals.
     *@param lobbyName the name of the lobby
     *@param numberOfPlayers the number of players in the lobby
     *@param numberOfCommonGoals the number of common goals in the lobby
     */
    public LobbyController(String lobbyName, int numberOfPlayers, int numberOfCommonGoals) {
        this.numberOfPlayers = numberOfPlayers;
        this.lobbyName = lobbyName;
        this.numberOfCommonGoals = numberOfCommonGoals;
        start = false;
        this.lock = new Object();
    }

    /**
     *Runs the lobby controller in a separate thread.
     *It waits until the number of players in the lobby reaches the desired number, then starts the game.
     */
    @Override
    public void run() {
        synchronized (lock) {
            while (nicknameList.size() < numberOfPlayers) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Number of players reached up");
        try {
            startGame();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *Starts the game in the lobby.
     *It notifies all the players in the lobby that the game has started.
     *It initializes the match and continues with the turns until the match ends.
     *@throws IOException if an I/O error occurs
     */
    public void startGame() throws IOException {
        System.out.println("START OF THE MATCH!");
        start = true;
        for (ClientHandler client : clientHandlers) { //communicate to all the players the start of the game
            client.sendMessage(new StartGameMessage());
        }
        try {
            initializeMatch();
        } catch (ExceedNumberOfPlayersException | ExceededLibraryLimitException | InvalidNumberOfPlayersException e) {
            System.err.println(e.getMessage());
        }
        while (!match.isEnded()) {
            for (int i = 0; i < numberOfPlayers; i++) {
                for (int j = 0; j < numberOfPlayers; j++) {  //stamp for all the players all the information
                    final ClientHandler clientHandler = clientHandlers.get(j);
                    if (clientHandler != null) {
                        showTableStartTurn(clientHandler);
                    }
                }
                if (match.getBoard().isUpgradeBoard()) match.getBoard().setUpgradeBoard(false); //to notify to the GUI Tablecontroller that the Board has been updated

                if ((clientHandlers.get(i) != null) && (clientHandlers.get(i).getNickname().equals(nicknameList.get(i)))) { //in case of disconnections of one or more players
                    if (clientHandlers.stream().filter(Objects::nonNull).count() == 1) {
                        System.out.println("WAITING FOR ONE MORE PLAYER");
                        clientHandlers.get(i).sendMessage(new ClientErrorMessage("You have to wait at least one other players to continue the game"));
                    }
                    while (clientHandlers.stream().filter(Objects::nonNull).count() == 1) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                        }

                    }
                    System.out.println("\nTHE PLAYER " + clientHandlers.get(i).getNickname() + "START THE TURN\n");
                    activeTurnController = new TurnController(clientHandlers.get(i),
                            match.getPlayers().get(clientHandlers.indexOf(clientHandlers.get(i))), match);
                    activeTurnController.startTurn();
                    activeTurnController = null; //for possible errors
                    System.out.println("\nTHE PLAYER" + nicknameList.get(i) + "HAS FINISHED THE TURN");
                }
                if (clientHandlers.stream().allMatch(Objects::isNull)) { //if all the players are disconnected
                    match.gameEnded();
                }
            }
            if(!match.isEnded()) System.out.println("\nTHE TURNS ARE FINISHED, SO RESTART FROM THE FIRST PLAYER\n");
        }
        if (!(clientHandlers.stream().allMatch(Objects::isNull))) {
            endGame();
        }
    }

    /**
     *Sends the table state to the client at the start of their turn.
     *@param clientHandler the client handler associated with the player
     */
    public void showTableStartTurn(ClientHandler clientHandler) {
        ArrayList<Block[][]> othersLibrary = new ArrayList<>();
        Player currentPlayer = getPlayer(clientHandler);
        ArrayList<Player> otherPlayers = new ArrayList<>();
        for (int i = 0; i < match.getPlayers().size(); i++) {
            if (!(match.getPlayers().get(i).getNickName()).equals(currentPlayer.getNickName())) {
                otherPlayers.add(match.getPlayers().get(i));
            }
        }
        int[] othersPoints = new int[otherPlayers.size()];
        String[] othersNickName = new String[otherPlayers.size()];
        for (int i = 0; i < otherPlayers.size(); i++) {
            othersLibrary.add(otherPlayers.get(i).getLib().getMatrix());
            othersPoints[i] = otherPlayers.get(i).getOtherPoints();
            othersNickName[i] = otherPlayers.get(i).getNickName();
        }
        int[] commonGoals = new int[match.getCommonGoals().size()];
        int[] archivedNumb = new int[match.getCommonGoals().size()];
        int[] achievedCG = new int[match.getCommonGoals().size()];
        for (int i = 0; i < match.getCommonGoals().size(); i++) {
            commonGoals[i] = match.getCommonGoals().get(i).getID();
            archivedNumb[i] = match.getCommonGoals().get(i).rankingPlayer.size();  //num of tile on each common Goals
            achievedCG[i] = match.getCommonGoals().get(i).getPosition(currentPlayer);  //return position on each common Goals
        }
        clientHandler.sendMessage(new StampStartTurnMessage(currentPlayer.getNickName(), currentPlayer.getLib().getMatrix(), currentPlayer.getPoints(), match.getBoard().getMatrix(), match.getSecretGoal(currentPlayer), achievedCG, othersLibrary, othersPoints, othersNickName, commonGoals, archivedNumb, match.getBoard().isUpgradeBoard()));
    }

    /**
     *Sends the end game results to all the players when the game is finished.
     */
    public void endGame() {
        System.out.println("THE GAME IS FINISH!!!");
        String[] nickNames = new String[numberOfPlayers];
        int[][] points = new int[numberOfPlayers][5];
        int[] rank = setRankingPoints();
        for (int i = 0; i < rank.length; i++) {
            for (int j = 0; j < numberOfPlayers; j++) {
                if (rank[i] == match.getPlayers().get(j).getPoints()) {
                    nickNames[i] = match.getPlayers().get(j).getNickName();
                    points[i][0] = match.getPlayers().get(j).getPointsSecretGoal();
                    points[i][1] = match.getPlayers().get(j).getPointsCommonGoal();
                    points[i][2] = match.getPlayers().get(j).getPointsAdjacent();
                    points[i][3] = match.getPlayers().get(j).getPointsFinal();
                    points[i][4] = match.getPlayers().get(j).getPoints();
                }
            }
        }
        for (ClientHandler handler : clientHandlers) {
            handler.sendMessage(new StampEndGameMessage(points, nickNames));
        }
    }

    /**
     *Calculates the ranking points for the players in the lobby.
     *@return an array containing the ranking points for each player
     */
    private int[] setRankingPoints() {
        int[] rank = new int[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            rank[i] = match.getPlayers().get(i).getPoints();
        }
        //Bubble Sort Algorithm
        for (int i = 0; i < rank.length - 1; i++) {
            for (int j = 0; j < rank.length - i - 1; j++) {
                if (rank[j] > rank[j + 1]) {
                    int temp = rank[j];
                    rank[j] = rank[j + 1];
                    rank[j + 1] = temp;
                }
            }
        }
        return rank;
    }

    /**
     *Initializes the match in the lobby by creating a new match object and adding players to it.
     *@throws ExceededLibraryLimitException if the library limit is exceeded
     *@throws InvalidNumberOfPlayersException if the number of players is invalid
     *@throws ExceedNumberOfPlayersException if the number of players exceeds the limit
     */
    public void initializeMatch() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException, ExceedNumberOfPlayersException {
        this.match = new Match(numberOfPlayers, numberOfCommonGoals);
        for (ClientHandler client : clientHandlers) {
            match.addPlayer(new Player(client.getNickname(), match));
        }
    }

    /**
     *Adds a nickname to the lobby's nickname list.
     *@param nickname the nickname to add
     */
    public void addNickname(String nickname) {
        synchronized (lock) {
            nicknameList.add(nickname);
            lock.notifyAll();
        }
    }

    /**
     * Removes a client handler from the lobby. If the lobby has already started the game, the corresponding client handler
     * is set to null in the client handlers list. Otherwise, the client handler is removed from both the nickname list and
     * the client handlers list.
     * @param clientHandler the client handler to remove
     */
    public void removeClientHandlerFromLobby(ClientHandler clientHandler) {
        if (start) {
            int index = clientHandlers.indexOf(clientHandler);
            clientHandlers.set(index, null);
        } else {
            nicknameList.remove(clientHandler.getNickname());
            clientHandlers.remove(clientHandler);
        }
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public ArrayList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public ArrayList<String> getNicknameList() {
        return this.nicknameList;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public Match getMatch() {
        return match;
    }

    public TurnController getActiveTurnController() {
        return activeTurnController;
    }

    public Player getPlayer(ClientHandler handler) {
        return match.getPlayers().get(clientHandlers.indexOf(handler));
    }

    public void addClientHandler(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
    }

}