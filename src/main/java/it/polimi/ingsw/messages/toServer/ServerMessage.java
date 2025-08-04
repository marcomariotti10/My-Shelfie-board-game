package it.polimi.ingsw.messages.toServer;


import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerMessage extends Remote, Serializable {

}
