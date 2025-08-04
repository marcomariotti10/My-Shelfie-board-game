package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.messages.toClient.ColumnInsertionMessage;
import it.polimi.ingsw.server.ClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class SetColumnMessage implements ServerMessage {
    int column;
    int size;

    public SetColumnMessage(int col, int dim) {
        this.column = col;
        this.size = dim;
    }

    public int getColumn(){
        return column;
    }

    public int getSize(){
        return size;
    }
}
