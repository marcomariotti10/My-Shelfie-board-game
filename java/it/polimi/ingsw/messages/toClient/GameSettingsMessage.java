package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.messages.toServer.SetLobbySettingMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameSettingsMessage implements ClientMessage{
    final String lobbyName;
    public GameSettingsMessage(String lobbyName) {
        this.lobbyName = lobbyName;
    }
    public String getLobbyName(){return this.lobbyName;}
}
