package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.toServer.ServerMessage;
import it.polimi.ingsw.server.ClientHandlerRMIInterface;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * The class responsible for sending messages to the server.
 * It handles the communication between the client and the server, either through RMI or socket connection.
 *
 * @author Gabriele Marra
 * @author Marco Mariotti
 */
public class MessageHandler implements Serializable {
    private ObjectOutputStream os = null;
    private ClientHandlerRMIInterface serverClientHandler = null;

    /**
     * Constructs a new MessageHandler object for socket communication.
     *
     * @param os The ObjectOutputStream used for sending messages.
     */
    public MessageHandler(ObjectOutputStream os) {
        this.os = os;
    }

    /**
     * Constructs a new MessageHandler object for RMI communication.
     *
     * @param serverClientHandler The RMI interface for handling server messages.
     */
    public MessageHandler(ClientHandlerRMIInterface serverClientHandler) {
        this.serverClientHandler = serverClientHandler;
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to be sent.
     */
    public void sendMessageToServer(ServerMessage message) {
        if (serverClientHandler != null) { //is RMI
            try {
                serverClientHandler.handleServerMessage(message);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        } else if (os != null) { //is Socket
            try {
                os.writeObject(message);
                os.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } //else do nothing
    }
}
