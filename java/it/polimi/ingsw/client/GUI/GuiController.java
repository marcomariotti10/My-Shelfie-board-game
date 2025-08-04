package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientRMI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.GUI.ScenesControllers.LoginController;
import it.polimi.ingsw.client.GUI.ScenesControllers.TableController;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.messages.toClient.*;
import it.polimi.ingsw.messages.toServer.DrawBlocksMessage;
import it.polimi.ingsw.messages.toServer.SetBlockMessage;
import it.polimi.ingsw.messages.toServer.SetColumnMessage;
import it.polimi.ingsw.server.ClientHandlerRMIInterface;
import it.polimi.ingsw.server.model.Block;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 *GuiController used by the GUI to interact with the user during the match.
 *@author Andrea Gollo, Francesco Foresti
 */

public class GuiController extends Observable implements Serializable, Controller {

    private MessageHandler messageHandler;
    private ObjectOutputStream os = null;
    private ClientHandlerRMIInterface serverClientHandler = null;
    private ClientSocket clientSocket;
    private ClientRMI clientRMI;
    private TableController tableController;
    private LoginController loginController;
    private final Integer lock;

    private int phase;


    private boolean loginCheck = false;


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    public void setTableController(TableController tableController) {
        synchronized (lock){

            this.tableController = tableController;
            lock.notifyAll();
        }
        tableController.setLockGuiController(lock);
    }



    public GuiController(TableController tableController, LoginController loginController) {
        this.loginController = loginController;
        this.tableController = tableController;
        this.phase = 0;
        this.lock = 0;
    }

    /**
     * Method for changing the phase ad notifying the observer
     * @param phase
     */
    public void setPhase(int phase){
        this.phase = phase;
        setChanged();
        notifyObservers(phase);
    }

    /**
     * Method for creating client socket
     * @param hostName
     * @param portNumber
     */
    public void createClientSocket(String hostName, int portNumber) {
        clientSocket = new ClientSocket(hostName, portNumber, this);
    }

    /**
     * method for creating the client rmi
     * @param hostName
     * @param portNumber
     */
    public void createClientRMI(String hostName, int portNumber) {
        clientRMI = new ClientRMI(hostName, portNumber);
    }

    /**
     * method for creating the message handler
     */

    public void createMessageHandler() {
        if (clientSocket != null) {
            messageHandler = clientSocket.getMessageHandler();
        } else {
            messageHandler = new MessageHandler(clientRMI.getServerClientHandler());
        }
    }
    @Override
    public void handlePingMessage(PingMessage message) {

    }

    /**
     * method for handling login message int the gui
     * @param message login
     */
    @Override
    public void handleLoginMessage(LoginMessage message) {
        //Fase 1
        setPhase(1);
        System.out.println("sono in guicontroller -> handlelogin");
    }

    /**
     * method for handling ask block message int the gui
     * @param message askblock
     */
    @Override
    public void handleAskBlockMessage(AskBlockMessage message) {
        System.out.println("sono handleAskBlockMessage");
        final ArrayList<Block> colors = message.getColors();
        tableController.askBlockTime(colors);
        synchronized (lock){
            while (tableController.getPlayerThink() == 3){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Block block = tableController.getBlockChosenAsBlock();
        if (block != null){
            messageHandler.sendMessageToServer(new SetBlockMessage(block));
            System.out.println("SetBlockMessage spedito");
        }
        else {
            System.out.println("we are fucked! Tehee! XD");
        }
    }
    /**
     * method for handling exit message int the gui
     * @param message exit
     */
    @Override
    public void handleClientExitMessage(ClientExitMessage message) {
        notificationPopup("Exit Message", "Your connection is closed");
        System.out.println(message.toString());
    }

    @Override
    public void handleClientErrorMessage(ClientErrorMessage message) {
        System.out.println(message.getMessage());
        errorPopup("ERROR!" , message.getMessage());
    }

    /**
     * Handles the ColumnInsertionMessage to perform column insertion in the game.
     *
     * @param message The ColumnInsertionMessage containing the column chosen and the size of the insertion.
     */
    @Override
    public void handleColumnInsertionMessage(ColumnInsertionMessage message) {
        System.out.println("Handling ColumnInsertionMessage");
        tableController.columnTime();
        synchronized (lock) {
            while (tableController.getPlayerThink() == 2) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        messageHandler.sendMessageToServer(new SetColumnMessage(tableController.getColumnChosen(), message.getSize()));
        System.out.println("SetColumnMessage sent");
    }

    /**
     * Handles the DrawBlockMessage to perform block drawing in the game.
     *
     * @param message The DrawBlockMessage containing the request to draw blocks.
     */
    @Override
    public void handleDrawBlockRequest(DrawBlockMessage message) {
        System.out.println("Handling DrawBlockRequest");
        tableController.drawTime();
        synchronized (lock) {
            while (tableController.getPlayerThink() == 1) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Set<ArrayList<Integer>> positions = new HashSet<>();
        positions.addAll(tableController.getBlocksToDrawCoord());
        System.out.println(positions);
        System.out.println("Drawn blocks: " + tableController.getBlocksToDrawCoord());
        messageHandler.sendMessageToServer(new DrawBlocksMessage(positions));
    }

    /**
     * Handles the EndGameMessage to handle the end of the game.
     *
     * @param message The EndGameMessage containing the end game information.
     */
    @Override
    public void handleEndGameMessage(EndGameMessage message) {
        // Notify the start of the last round because someone completed their library
        notificationPopup("Last Round", message.getString());
        tableController.endGamePoint(message.getWinnerName());
        System.out.println(message.getString());
    }

    /**
     * Handles the StampEndGameMessage to handle the final end of the game.
     *
     * @param message The StampEndGameMessage containing the final end game information.
     */
    @Override
    public void handleStampEndGameMessage(StampEndGameMessage message) {
        tableController.endGamePopup(message.getNickNames(), message.getPoints());
        //Fase 6
        /*setPhase(6);
        System.out.println("sono in guicontroller -> handlelogin");
        Platform.runLater(() -> tableController.rankingScene(message.getNickNames(), message.getPoints()));*/
        //Platform.runLater(() -> tableController.openGameOverRankingPopup(message.getNickNames(), message.getPoints()));
    }

    /**
     * Handles the StampEndTurnMessage to handle the end of a turn.
     *
     * @param message The StampEndTurnMessage indicating the end of a turn.
     */
    @Override
    public void handleStampEndTurnMessage(StampEndTurnMessage message) {
        tableController.endTurn();
    }

    /**
     * Handles the StampMiddleTurnMessage to handle the middle of a turn.
     *
     * @param message The StampMiddleTurnMessage containing the information about the middle of a turn.
     */
    @Override
    public void handleStampMiddleTurnMessage(StampMiddleTurnMessage message) {
        tableController.setLibraryPlayerGrid(message.getLibrary());
    }

    /**
     * Handles the EnterNicknameMessage to handle the request for entering a nickname.
     *
     * @param message The EnterNicknameMessage indicating the need to enter a nickname.
     */
    @Override
    public void handleEnterNicknameMessage(EnterNicknameMessage message) {
        System.out.println("Enter nickname");
        setPhase(3);
    }

    /**
     * Handles the StampStartTurnMessage to handle the start of a turn.
     *
     * @param message The StampStartTurnMessage containing the start turn information.
     */
    @Override
    public void handleStampStartTurnMessage(StampStartTurnMessage message) {
        System.out.println("Handling StampStartTurnMessage");
        try {
            if (tableController == null) {
                System.out.println("TABLE NULL");
                handleStartGameMessage(new StartGameMessage());
            } else {
                System.out.println("TABLE NOT NULL");
            }
            System.out.println("Player name: " + message.getName());

            tableController.setTable(message.getName(), message.getPlayerlibrary(),
                    message.getPlayerPoints(), message.getGameboard(), message.getSecret(),
                    message.getAchivedCG(), message.getOtherplayerslibrary(),
                    message.getOtherplayerspoints(), message.getOtherplayersNickName(),
                    message.getCommongoals(), message.getAchivedNum(), message.isBoardUpgrade());
        } catch (InterruptedException e) {
            errorPopup("Thread Interruption", e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println("StampStartTurnMessage handled");
    }

    @Override
    public void handleGameSettingsMessage(GameSettingsMessage message) {
        System.out.println("sono arrivato!!!!!XD");
        setPhase(2);
    }

    @Override
    public void handleStartGameMessage(StartGameMessage message) {
        System.out.println("sono handleStartGameMessage");
        setPhase(5);
        synchronized (lock){
            while (tableController == null){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void handleWaitingMessage(WaitingMessage message) {
        setPhase(4);
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public ClientRMI getClientRMI() {
        return clientRMI;
    }

    /**
     * Displays an error popup with the given title and message.
     *
     * @param title   The title of the error popup.
     * @param message The error message to be displayed.
     */
    public void errorPopup(String title, String message){
        Platform.runLater(() -> {
            System.out.println("sono errorPopup");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Displays a notification popup with the given title and message.
     *
     * @param title   The title of the notification popup.
     * @param message The notification message to be displayed.
     */
    public void notificationPopup(String title, String message) {
        System.out.println("sono notificationPopup");
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText("NOTIFY");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
