package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.messages.toClient.*;
import it.polimi.ingsw.messages.toServer.*;

import java.rmi.RemoteException;

/**
 * The RMI implementation of the client handler in the server.
 * It handles the communication between the server and the RMI client.
 * Extends the abstract class ClientHandler and implements the ClientHandlerRMIInterface.
 *
 * This class is used to manage the RMI communication with a client.
 * It provides methods for setting the client's client handler, server's client handler, and client controller.
 * It also handles incoming server messages and sends client messages.
 *
 * @author Gabriele Marra
 * @author Marco Mariotti
 */
public class ClientHandlerRMI extends ClientHandler implements ClientHandlerRMIInterface {
    public ClientHandlerRMIInterface clientClientHandler = null;
    public ClientHandlerRMIInterface serverClientHandler = null;
    private Controller clientController = null;
    private final String uuid = null;
    private boolean disconnected = false;

    /**
     * Constructs a new ClientHandlerRMI object with the specified server.
     *
     * @param server The server instance.
     * @throws RemoteException if a remote communication error occurs.
     */
    public ClientHandlerRMI(Server server) throws RemoteException {
        super(server);
    }

    /**
     * Sets the client's client handler.
     *
     * @param clientClientHandler The client's client handler.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void setClientClientHandler(ClientHandlerRMIInterface clientClientHandler) throws RemoteException {
        this.clientClientHandler = clientClientHandler;
    }

    /**
     * Sets the server's client handler.
     *
     * @param serverClientHandler The server's client handler.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void setServerClientHandler(ClientHandlerRMIInterface serverClientHandler) throws RemoteException {
        this.serverClientHandler = serverClientHandler;
    }

    /**
     * Sets the client controller.
     *
     * @param clientController The client controller.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void setClientController(Controller clientController) throws RemoteException {
        this.clientController = clientController;
    }

    /**
     * Handles a server message received from the server.
     *
     * @param message The server message to handle.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void handleServerMessage(ServerMessage message) throws RemoteException {
        serverClientHandler.handleMessage(message);
    }

    /**
     * Function to be called on the client to manage a Client Event.
     *
     * @param message The client message to send.
     */
    @Override
    public void sendMessage(ClientMessage message) {
        try {
            clientClientHandler.handleMessage(message);
        } catch (Exception e) {
            getServer().removeClientHandler(this);
            disconnected = true;
        }
    }

    /**
     * Starts the ping process to keep the connection alive with the client.
     */
    public void startPing() {
        System.out.println("Entered in start ping");
        while (!disconnected) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("disconnected");
            }
            sendMessage(new PingMessage());
        }
    }

    /**
     * Handles a client message received from the client.
     *
     * @param message The client message to handle.
     */
    @Override
    public void handleMessage(ClientMessage message) {
        clientController.handleMessage(message);
    }

    /**
     * Gets the UUID (Universally Unique Identifier) of the client.
     *
     * @return The UUID of the client.
     */
    public String getUuid() {
        return this.uuid;
    }
}
