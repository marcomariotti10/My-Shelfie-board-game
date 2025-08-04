package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.messages.toClient.ClientMessage;
import it.polimi.ingsw.messages.toServer.ServerMessage;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientHandlerRMIInterface extends Remote, Serializable {
     void setClientClientHandler(ClientHandlerRMIInterface clientClientHandler) throws RemoteException;
     void handleServerMessage(ServerMessage message) throws RemoteException;
     void handleMessage(ClientMessage message) throws RemoteException;
     void handleMessage(ServerMessage message) throws RemoteException;
     void setServerClientHandler(ClientHandlerRMIInterface serverClientHandler) throws RemoteException;
     void setClientController(Controller clientController) throws RemoteException;
}

