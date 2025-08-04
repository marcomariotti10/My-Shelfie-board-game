package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.messages.toClient.ClientErrorMessage;
import it.polimi.ingsw.messages.toClient.ClientMessage;
import it.polimi.ingsw.messages.toClient.GameSettingsMessage;
import it.polimi.ingsw.messages.toClient.LoginMessage;
import it.polimi.ingsw.exceptions.LobbyControllerAlreadyExist;
import it.polimi.ingsw.server.ClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CreateLobbyMessage implements ServerMessage {
    public String lobbyName;

    public CreateLobbyMessage(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName(){
        return lobbyName;
    }
}
