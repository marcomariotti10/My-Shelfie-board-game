package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.messages.toServer.SetNicknameMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EnterNicknameMessage implements ClientMessage{
    public EnterNicknameMessage() {
    }

}
