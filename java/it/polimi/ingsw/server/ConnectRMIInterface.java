package it.polimi.ingsw.server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectRMIInterface extends Remote, Serializable {
    void startNewConnection(String clientId) throws RemoteException;
    boolean isConnected(String clientId) throws RemoteException;
    void connectionWithClient() throws RemoteException;
}
