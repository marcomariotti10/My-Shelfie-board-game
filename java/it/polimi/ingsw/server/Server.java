package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.exceptions.LobbyControllerAlreadyExist;
import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *Represents a server that implements for the connection.
 *@author Andrea Golla,Gabriele Marra,Marco Mariotti
 */
public class Server implements Serializable {
    private final ArrayList<LobbyController> lobbyControllers = new ArrayList<>();
    private final int portSocket, portRMI;
    ConnectRMI connectRMI;

    /**
     *Constructs a Server object with the specified socket and RMI port.
     *@param portSocket The socket port for the server.
     *@param portRMI The RMI port for the server.
     */
    public Server(int portSocket, int portRMI) {
        this.portSocket = portSocket;
        this.portRMI = portRMI;
    }

    /**
     *Starts the RMI server by creating a registry and binding the ConnectRMI object.
     *@throws RemoteException if an RMI error occurs.
     */
    public void startRMIServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(portRMI);
        connectRMI = new ConnectRMI(this);
        registry.rebind("ConnectRMI", connectRMI);
    }

    /**
     *Starts the WebSocket server and accepts incoming connections.
     */
    public void startWebSocketServer() {
        final ExecutorService executor = Executors.newCachedThreadPool();
        try (
                ServerSocket serverSocket = new ServerSocket(portSocket)
        ) {
            System.out.println("SERVER SOCKET READY");
            while (true) {
                System.out.println("Waiting for connection");
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(clientSocket, this);
                    executor.submit(clientHandlerSocket);
                } catch (IOException e) {
                    System.out.println("ServerSocket restart");
                    serverSocket.close();
                    startWebSocketServer();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *Adds a LobbyController to the server's list of lobby controllers.
     *@param controller The LobbyController to be added.
     *@throws LobbyControllerAlreadyExist if a LobbyController with the same name already exists.
     */
    public void addLobbyController(LobbyController controller) throws LobbyControllerAlreadyExist {
        if (getLobbyController(controller.getLobbyName()) != null) throw new LobbyControllerAlreadyExist();
        else lobbyControllers.add(controller);
    }

    /**
     *Retrieves a LobbyController based on the lobby name.
     *@param lobbyName The name of the lobby.
     *@return The LobbyController object corresponding to the lobby name, or null if not found.
     */
    public LobbyController getLobbyController(String lobbyName) {
        List<LobbyController> q = lobbyControllers.stream().filter((c) -> c.getLobbyName().equals(lobbyName)).toList();
        if (q.size() == 0) return null;
        return q.get(0);
    }

    /**
     *Checks if a LobbyController with the specified lobby name already exists.
     *@param lobbyName The name of the lobby to check.
     *@throws LobbyControllerAlreadyExist if a LobbyController with the same name already exists.
     */
    public void checkLobbyName(String lobbyName) throws LobbyControllerAlreadyExist {
        for (LobbyController lobby : lobbyControllers) {
            if (lobby.getLobbyName().equals(lobbyName)) throw new LobbyControllerAlreadyExist();
        }
    }

    /**
     *Removes a ClientHandler from the server's lobby controllers.
     *@param removedClienthandler The ClientHandler to be removed.
     */
    public void removeClientHandler(ClientHandler removedClienthandler) {
        System.out.println("Removed " + removedClienthandler);
        for (LobbyController lobbyController : lobbyControllers) {
            if (lobbyController.getClientHandlers().contains(removedClienthandler)) {
                lobbyController.removeClientHandlerFromLobby(removedClienthandler);
                if (removedClienthandler instanceof ClientHandlerRMI) {
                    connectRMI.disconnect(((ClientHandlerRMI) removedClienthandler).getUuid());
                }
                if ((lobbyController.getClientHandlers().size() == 0) || (lobbyController.getClientHandlers().stream().allMatch(Objects::isNull))) {
                    System.out.println("LOBBY REMOVED");
                    lobbyControllers.remove(lobbyController);
                }
                break;
            }
        }
    }

    /**
     *Checks if a ClientHandler is active in any of the server's lobby controllers.
     *@param clientHandler The ClientHandler to check.
     *@return true if the ClientHandler is active in any lobby controller, false otherwise.
     */
    public boolean isClientHandlerActive(ClientHandler clientHandler) {
        return lobbyControllers.stream().anyMatch(lobbyController -> lobbyController.getClientHandlers().contains(clientHandler));
    }

    /**
     *Reads a line of input from the user.
     *@return The input read from the user, or null if an error occurs.
     */
    public static String getInput() {
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     *Gets the RMI port of the server.
     *@return The RMI port.
     */
    public int getPortRMI() {
        return portRMI;
    }

    /**
     *Retrieves all available IP addresses.
     *@return The IP address as a string.
     */
    static String getAll() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                    return address.getHostAddress();
                }
            }
        }
        return "";
    }

    /**
     *Ask the player the port values for socket and RMI,and show the IP address.
     */
    public static void main(String[] args) throws IOException {
        String localIp = getAll();
        System.setProperty("java.rmi.server.hostname", localIp);

        int socketport = 1234;
        int rmiport = 1099;
        String tempstring;
        int tempint;

        System.out.println("Insert the port value for the socket connection, or click enter to use the standard port");
        boolean canParse = false;
        while (!canParse) {
            tempstring = getInput();
            if ("".equals(tempstring)) canParse = true;
            else {
                try {
                    tempint = Integer.parseInt(tempstring);
                    if (tempint >= 1024 && tempint <= 65535) {
                        socketport = tempint;
                        canParse = true;
                    } else {
                        System.err.println(tempint + " it's not acceptable, put a value between 1024 and 65535");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Your input is not valid");
                }
            }
        }
        System.out.println("Insert the port value for the rmi connection, or click enter to use the standard port");
        canParse = false;
        while (!canParse) {
            tempstring = getInput();
            if (Objects.equals(tempstring, "")) canParse = true;
            else {
                try {
                    tempint = Integer.parseInt(tempstring);
                    if (tempint <= 65535 && tempint >= 1024 && tempint != socketport) {
                        rmiport = tempint;
                        canParse = true;
                    } else {
                        System.err.println(tempint + " it's not acceptable, put a value between 1024 and 65535 and different from the socketport");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("your input is not valid");
                }
            }
        }
        // Obteins the IP address of the host
        System.out.println("IP Address: " + localIp);
        System.out.println("Socket port : " + socketport);
        System.out.println("RMI port : " + rmiport);
        Server server = new Server(socketport, rmiport);
        server.startRMIServer();
        server.startWebSocketServer();
    }
}