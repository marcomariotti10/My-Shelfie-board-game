package it.polimi.ingsw.client.GUI.ScenesControllers;


import it.polimi.ingsw.client.GUI.GuiController;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.messages.toServer.AccessLobbyMessage;
import it.polimi.ingsw.messages.toServer.CreateLobbyMessage;
import it.polimi.ingsw.messages.toServer.SetLobbySettingMessage;
import it.polimi.ingsw.messages.toServer.SetNicknameMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static it.polimi.ingsw.common.Constants.MAX_CHAR;

/**
 * Is the controller for all the fxml files related to the setup of the game
 * @author Francesco Foresti, Andrea Gollo
 */


public class LoginController implements Serializable {
    @FXML
    public Button socketButton;
    public Button rmiButton;
    public TextField numberOfPlayers;
    public TextField numberOfCommon;
    public Button settingsButton;
    public TextField nickName;
    public Button nickButton;
    public TextField ipAddress;
    public TextField port;

    @FXML
    private Button joinButton;

    @FXML
    private Button createButton;

    @FXML
    private TextField existingGameName;

    @FXML
    private TextField newGameName;

    private GuiController guiController;

    private GUI gui;



    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void setGuiController(GuiController guiController){
        this.guiController = guiController;
    }

    /**
     * Create a new game
     * @param event the button new game is pressed
     */
    public void createNewGame(ActionEvent event) {
        if(newGameName.getText() !=null && !newGameName.getText().equals("") && newGameName.getText().length()<=MAX_CHAR){
            System.out.println("ho premuto il bottone e mando il mex, nome partita: "+newGameName.getText());
            guiController.getMessageHandler().sendMessageToServer(new CreateLobbyMessage(newGameName.getText()));
            gui.setGameName(newGameName.getText());
        }
        else{
            gui.notificationPopup("Error", "Game name cannot be empty or exceed "+MAX_CHAR +" characters");
        }
    }

    /**
     * Join a  game
     * @param event the button join game is pressed
     */
    public void joinGame(ActionEvent event) {
        if(existingGameName.getText() !=null && !existingGameName.getText().equals("") && existingGameName.getText().length() <= MAX_CHAR){
            guiController.getMessageHandler().sendMessageToServer(new AccessLobbyMessage(existingGameName.getText()));
        }
        else{
            gui.notificationPopup("Error", "Game name cannot be empty or exceed "+MAX_CHAR+" characters");
        }
    }

    /**
     * The method called to create a rmi connection
     */
    private void rmiCreation(){  //metti a posto per rmi da cli
        guiController.createClientRMI(ipAddress.getText(), Integer.parseInt(port.getText()));
        System.out.println("Dopo creazione rmi");
        try{
            System.out.println("sto entrando nel try");
            guiController.getClientRMI().startingPoint(guiController);
            System.out.println("ho finito startingpoint");
            guiController.createMessageHandler();
            System.out.println("creato message handler");
        } catch (NotBoundException | RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * The method called to create a socket connection
     */
    private void clientSocketCreation(){
        if(!ipAddress.getText().equals("")){
            guiController.createClientSocket(ipAddress.getText(), Integer.parseInt(port.getText()));
            System.out.println("sto entrando nel thread di startingpoint");
            try {
                System.out.println("startingpoint ora");
                guiController.getClientSocket().startingPoint();
                System.out.println("dopo starting point");
                guiController.createMessageHandler();
                System.out.println("ho finito lo startingpoint");

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    /**
     *
     * @param actionEvent button pressed to create socket connection
     */
    public void socketCreate(ActionEvent actionEvent) {
        if(checkPort() && checkIp()){
            clientSocketCreation();
        }
    }

    /**
     *
     * @param actionEvent button pressed to create rmi connection
     */
    public void rmiCreate(ActionEvent actionEvent) {
        if(checkPort() && checkIp()){
            rmiCreation();
        }
    }

    /**
     * Method for deciding number of players and common goals
     * @param event button pressed to confirm the settings
     */
    public void setSettings(ActionEvent event) {
        if(numberOfPlayers.getText()!= null && (numberOfPlayers.getText().equals("2") || numberOfPlayers.getText().equals("3") ||
                numberOfPlayers.getText().equals("4")) && (numberOfCommon.getText() != null && (numberOfCommon.getText().equals("1") || numberOfCommon.getText().equals("2")))){
            guiController.getMessageHandler().sendMessageToServer(new SetLobbySettingMessage(gui.getGameName(),
                    Integer.parseInt(numberOfPlayers.getText()), Integer.parseInt(numberOfCommon.getText())));
        }
        else{
            gui.notificationPopup("Error","Max players 4, max common goals 2");
        }
    }

    /**
     * Method called for setting the name
     * @param event button pressed to confirm the name
     */
    public void selectNickname(ActionEvent event) {
        if(nickName.getText() !=null && !nickName.getText().equals("") && nickName.getText().length() <= MAX_CHAR){
            guiController.getMessageHandler().sendMessageToServer(new SetNicknameMessage(nickName.getText()));
        }
        else{
            gui.notificationPopup("Error", "Name cannot be empty or exceed "+MAX_CHAR+" characters");
        }
    }

    /**
     * Method called for checking syntactic correctness of the port
     * @return true if port is correct
     */
    private boolean checkPort() {
                if (port.getText() == null || port.getText().isEmpty() || Integer.parseInt(port.getText()) <= 1024 || Integer.parseInt(port.getText()) >= 65535) {
                    gui.notificationPopup("Error", "Port is incorrect");
                    return false;
                }
                else{
                    return true;
                }
    }

    /**
     * Method called for checking syntactic correctness of the ip
     * @return true if the ip is correct
     */
    private boolean checkIp() {
            if (ipAddress.getText() != null && ipAddress.getText().contains(".") && !ipAddress.getText().isEmpty() && !ipAddress.getText().equals("")) {
                String[] splittedIp = ipAddress.getText().split("\\.");
                boolean validNumber = true;
                if (splittedIp.length == 4) {
                    for (int i = 0; i < splittedIp.length && validNumber; i++) {
                        try {
                            int number = Integer.parseInt(splittedIp[i]);
                            if (number > 999 || number < 0) {
                                validNumber = false;
                            }
                        } catch (Exception e) {
                            validNumber = false;
                        }
                    }
                    if(!validNumber){
                        gui.notificationPopup("Error", "Ip Server incorrect");
                    }
                    return validNumber;
                }
            }
            gui.notificationPopup("Error", "Ip Server incorrect");
            return false;
    }
}