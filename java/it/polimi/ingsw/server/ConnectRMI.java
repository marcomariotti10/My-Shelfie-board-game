package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.toClient.LoginMessage;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *The ConnectRMI class represents a remote object that provides RMI (Remote Method Invocation) connectivity
 *and communication functionality between the server and clients.
 *It extends the UnicastRemoteObject class to support remote object activation and deactivation.
 @author Gabriele Marra, Marco Mariotti
 */
public class ConnectRMI extends UnicastRemoteObject implements ConnectRMIInterface, Serializable {
    public Server server = null;
    ArrayList<String> connectedClients = new ArrayList<>();

    /**
     *The server object associated with the ConnectRMI instance.
     */
    public ConnectRMI(Server server) throws RemoteException {
        this.server = server;
    }

    /**
     *Checks if the specified client ID is connected to the server.
     *@param clientId The ID of the client to check.
     *@return true if the client is connected, false otherwise.
     *@throws RemoteException if a remote communication error occurs.
     */
    public boolean isConnected(String clientId) throws RemoteException {
        return connectedClients.contains(clientId);
    }

    /**
     *Used to check the connection with the RMI client.
     */
    @Override
    public void connectionWithClient() throws RemoteException {
    }

    /**
     *Disconnects the client with the specified ID from the server.
     *@param clientId The ID of the client to disconnect.
     */
    public void disconnect(String clientId){
        connectedClients.remove(clientId);
    }


    /**
     *Starts a new connection with the client identified by the specified ID.
     *This method creates a new thread to handle the connection and communication with the client.
     *It uses RMI registry to bind the client handler and adds the client ID to the list of connected clients.
     *It sends a login message to the client and starts a ping process.
     *@param clientId The ID of the client to start a connection with.
     *@throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void startNewConnection(String clientId) throws RemoteException {
        Thread t = new Thread(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(server.getPortRMI());
                ClientHandlerRMI serverClientHandler = new ClientHandlerRMI(server);
                registry.rebind("ClientHandlerRMI" + clientId, serverClientHandler);
                serverClientHandler.setServerClientHandler(serverClientHandler);
                connectedClients.add(clientId);
                boolean stop = false;
                while (!stop) {
                    Thread.sleep(100);
                    if (serverClientHandler.clientClientHandler != null) {
                        serverClientHandler.sendMessage(new LoginMessage());
                        stop = true;
                    }
                }
                serverClientHandler.startPing();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        });
        t.start();
    }

}

