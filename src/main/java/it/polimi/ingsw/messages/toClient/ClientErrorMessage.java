package it.polimi.ingsw.messages.toClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientErrorMessage implements ClientMessage{
    public String message;
    public ClientErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}