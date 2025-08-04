package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.messages.toClient.ClientExitMessage;
import it.polimi.ingsw.messages.toClient.ClientMessage;
import it.polimi.ingsw.messages.toClient.EnterNicknameMessage;
import it.polimi.ingsw.server.ClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccessLobbyMessage implements ServerMessage{
    public String LobbyName;
    public AccessLobbyMessage(String LobbyName) {
        this.LobbyName = LobbyName;
    }

    public String getLobbyName(){
        return LobbyName;
    }

}
