package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLIcontroller;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.GuiController;
import it.polimi.ingsw.server.*;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

/**
 * The client-side class for RMI connection.
 * This class is responsible for establishing and managing the RMI connection with the server.
 * @author Gabriele Marra, Marco Mariotti, Francesco Foresti
 */
public class ClientRMI implements Serializable {
    ClientHandlerRMIInterface serverClientHandler = null;
    final String hostName;
    final int portNumber;
    private final String clientId;

    /**
     * Constructs a new ClientRMI object.
     *
     * @param hostName   The hostname of the server.
     * @param portNumber The port number of the server.
     */
    public ClientRMI(String hostName, int portNumber) {
        this.clientId = UUID.randomUUID().toString();
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Starts the RMI ping to check if server disconected.
     *
     * @param connectRMIInterface The RMI Interface
     */
    private void startServerPing(ConnectRMIInterface connectRMIInterface){
        Thread t = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.err.println("FATAL ERROR");
                    System.exit(250);
                }
                try {
                    connectRMIInterface.connectionWithClient();
                } catch (Exception e) {
                    System.err.println("FATAL ERROR");
                    System.exit(250);
                }
            }
        });
        t.start();
    }

    /**
     * Starts a 5 sec timer that restart the client when connections wasn't done
     */
    private void startTimout(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(serverClientHandler == null){
                System.err.println("Cannot connect to this server");
                ClientCLI clientCLI = new ClientCLI();
                clientCLI.start();
            }
        });
        t.start();
    }
    private void startTimoutGUI(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(serverClientHandler == null){
                System.err.println("Cannot connect to this server");
                System.exit(255);
            }
        });
        t.start();
    }

    public String getClientId() {
        return clientId;
    }

    /**
     * Starts the RMI connection for CLI.
     *
     * @throws RemoteException   If a remote communication error occurs.
     * @throws NotBoundException If the specified name is not currently bound.
     */
    public void startingRMI() throws RemoteException, NotBoundException {//per la cli
        startTimout();
        Registry registry = LocateRegistry.getRegistry(hostName,portNumber);
        ConnectRMIInterface connectRMIInterface = (ConnectRMIInterface) registry.lookup("ConnectRMI");
        connectRMIInterface.startNewConnection(getClientId());
        while (!connectRMIInterface.isConnected(getClientId())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        serverClientHandler = (ClientHandlerRMIInterface) registry.lookup("ClientHandlerRMI" + clientId);
        ClientHandlerRMIInterface clientClientHandler = new ClientHandlerRMI(null); // se non funziona magari è perchè prima qua gli passavamo il serverClientHandler.getServerfromInterface
        Controller controller = new CLIcontroller(serverClientHandler);
        clientClientHandler.setClientController(controller);
        serverClientHandler.setClientController(controller);

        clientClientHandler.setServerClientHandler(serverClientHandler);
        serverClientHandler.setClientClientHandler(clientClientHandler);

        startServerPing(connectRMIInterface);
    }


    /**
     * Starts the RMI connection for CLI.
     *
     * @throws RemoteException   If a remote communication error occurs.
     * @throws NotBoundException If the specified name is not currently bound.
     */
    public void startingPoint(GuiController guiController) throws RemoteException, NotBoundException {//per la gui
        startTimoutGUI();
        Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
        System.out.println(hostName +" "+ portNumber);
        System.out.println("prima di lookup dentro gui");
        ConnectRMIInterface connectRMIInterface = (ConnectRMIInterface) registry.lookup("ConnectRMI");
        System.out.println("dopo lookup");
        connectRMIInterface.startNewConnection(getClientId());
        while (!connectRMIInterface.isConnected(this.getClientId())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("CONNECT RMI FUNZIONA");
        serverClientHandler = (ClientHandlerRMIInterface) registry.lookup("ClientHandlerRMI" + clientId);
        ClientHandlerRMIInterface clientClientHandler = new ClientHandlerRMI(null); // se non funziona magari è perchè prima qua gli passavamo il serverClientHandler.getServerfromInterface

        clientClientHandler.setClientController(guiController);
        serverClientHandler.setClientController(guiController);

        clientClientHandler.setServerClientHandler(serverClientHandler);
        serverClientHandler.setClientClientHandler(clientClientHandler);

        startServerPing(connectRMIInterface);
    }

    /**
     * Starts the RMI connection for CLI.
     *
     * @throws RemoteException   If a remote communication error occurs.
     * @throws NotBoundException If the specified name is not currently bound.
     */
    public ClientHandlerRMIInterface getServerClientHandler(){
        return serverClientHandler;
    }
}

