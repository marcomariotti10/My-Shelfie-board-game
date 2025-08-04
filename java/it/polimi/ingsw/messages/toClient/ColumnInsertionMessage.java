package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.messages.toServer.SetColumnMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class ColumnInsertionMessage implements ClientMessage{
    final int size;
    public ColumnInsertionMessage(int size) {
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
}

