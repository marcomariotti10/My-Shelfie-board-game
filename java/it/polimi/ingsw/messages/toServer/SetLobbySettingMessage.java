package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.exceptions.LobbyControllerAlreadyExist;
import it.polimi.ingsw.messages.toClient.ClientErrorMessage;
import it.polimi.ingsw.messages.toClient.EnterNicknameMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controllers.LobbyController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SetLobbySettingMessage implements ServerMessage  {
    final String lobbyName;
    final int numberOfPlayers;
    final int numberOfCommonGoals;

    public SetLobbySettingMessage(String lobbyName, int numberOfPlayers, int numberOfCommonGoals) {
        this.lobbyName = lobbyName;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfCommonGoals = numberOfCommonGoals;
    }

    public String getLobbyName(){
        return lobbyName;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    public int getNumberOfCommonGoals(){
        return numberOfCommonGoals;
    }

}
