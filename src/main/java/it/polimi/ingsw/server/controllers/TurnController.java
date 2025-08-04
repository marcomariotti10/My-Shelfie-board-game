package it.polimi.ingsw.server.controllers;

import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.messages.toClient.*;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The controller that manages a single turn of the game, including all its phases and operations.
 * It handles the various phases of a turn, verifies their correctness, and manages message exchanges.
 * The turn phases are implemented as individual methods within this class.
 * It requires the ServerClientHandler (from which it retrieves the respective player class) obtained from the LobbyController, and the corresponding match as parameters.
 * It handles actions such as drawing blocks, choosing a column, selecting colors, and updating game state.
 * It also checks for common goal achievements and checks for the end of the game.
 *
 * @author Gabriele Marra
 * @author Marco Mariotti
 * @author Andrea Gollo
 * @author Francesco Foresti
 */
public class TurnController implements Serializable {

    private ArrayList<Block> drewBlocks;
    private Block toAdd;
    private final ClientHandler currentHandler;
    private final Player currentPlayer;
    private final Match match;
    private int selectedColumn;
    private final ArrayList<Player> otherPlayers = new ArrayList<>();
    // Previous state
    private Player initTurnPlayerState;
    private Board initTurnBoardState;

    /**
     * Constructs a new TurnController object with the specified current handler, current player, and match.
     *
     * @param currentHandler The current handler associated with the player's client.
     * @param currentPlayer The current player.
     * @param match The match instance.
     */
    public TurnController(ClientHandler currentHandler, Player currentPlayer, Match match) {
        this.selectedColumn = -1;
        this.currentHandler = currentHandler;
        this.currentPlayer = currentPlayer;
        this.match = match;
        this.toAdd = Block.NULL;
        this.drewBlocks = new ArrayList<>();

        // Initialize other players
        for (int i = 0; i < match.getPlayers().size(); i++) {
            if (!(match.getPlayers().get(i).getNickName()).equals(currentPlayer.getNickName())) {
                otherPlayers.add(match.getPlayers().get(i));
            }
        }

        // Create a copy of the player's library matrix
        Block[][] matrixLib = new Block[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                matrixLib[i][j] = currentPlayer.getLib().getMatrix()[i][j];
            }
        }

        // Create a copy of the match board matrix
        int rowsBoard = match.getBoard().getMatrix().length;
        int columnsBoard = match.getBoard().getMatrix()[0].length;
        Block[][] matrix = new Block[rowsBoard][columnsBoard];

        for (int i = 0; i < rowsBoard; i++) {
            for (int j = 0; j < columnsBoard; j++) {
                matrix[i][j] = match.getBoard().getMatrix()[i][j];
            }
        }

        // Create initial state objects for player and board
        initTurnPlayerState = new Player(currentPlayer.getNickName(), match, new Library(matrixLib), currentPlayer.getPoints());
        initTurnBoardState = new Board(matrix, match.getNumberOfPlayers(), match.getBoard().getBag());
        initTurnPlayerState.getLib().setPlayer(initTurnPlayerState);
    }

    /**
     * Starts the turn and performs the necessary actions and message exchanges.
     *
     * @throws RuntimeException if an error occurs during the turn.
     */
    public void startTurn() throws RuntimeException {
        Thread t = new Thread(() -> {
            if (currentHandler.getLobbyController().getMatch().isEnded()) {
                currentHandler.sendMessage(new EndGameMessage(currentHandler.getNickname()));
            }
            // Draw blocks
            currentHandler.sendMessage(new DrawBlockMessage());
            do {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (drewBlocks.isEmpty());

            // Choose column
            currentHandler.sendMessage(new ColumnInsertionMessage(drewBlocks.size()));
            do {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (selectedColumn == -1);

            // Select colors
            while (!drewBlocks.isEmpty()) {
                toAdd = Block.NULL;
                currentHandler.sendMessage(new StampMiddleTurnMessage(drewBlocks, currentPlayer.Lib.getMatrix()));
                currentHandler.sendMessage(new AskBlockMessage(drewBlocks));

                do {
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        System.err.println("Client disconnected when drawing blocks");
                    }
                } while (toAdd == Block.NULL);

                drewBlocks.remove(toAdd);

                try {
                    currentPlayer.Lib.addBlock(selectedColumn, toAdd);
                } catch (ExceededLibraryLimitException e) {
                    e.printStackTrace();
                }
            }

            // Send end turn message and check for common goals
            currentHandler.sendMessage(new StampEndTurnMessage(currentPlayer.Lib.getMatrix()));
            for (CommonGoalStrategy CG : match.getCommonGoals()) {
                if (CG.check(currentPlayer.Lib.getMatrix())) {
                    CG.addPlayer(currentPlayer);
                }
            }

            // Check for end of the game
            currentPlayer.checkEndGame();
        });

        t.start();

        while (t.isAlive()) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Check for disconnection
            if (!currentHandler.getServer().isClientHandlerActive(currentHandler)) {
                match.setBoard(initTurnBoardState);
                match.restorePlayer(initTurnPlayerState);
                System.out.println("Disconnected");
                t.interrupt();
                break;
            }
        }
    }

    /**
     * Returns the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the list of blocks drawn by the player.
     *
     * @param drewBlocks The list of blocks drawn by the player.
     */
    public void setDrewBlocks(ArrayList<Block> drewBlocks) {
        this.drewBlocks = drewBlocks;
        System.out.println("Blocks set");
    }

    /**
     * Sets the block to be added by the player.
     *
     * @param b The block to be added.
     */
    public void setToAddBlock(Block b) {
        this.toAdd = b;
    }

    /**
     * Sets the selected column for block placement.
     *
     * @param n The selected column.
     */
    public void setSelectedColumn(int n) {
        this.selectedColumn = n;
    }
}
