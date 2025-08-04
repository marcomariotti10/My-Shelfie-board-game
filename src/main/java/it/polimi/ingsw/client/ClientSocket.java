package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLIcontroller;
import it.polimi.ingsw.messages.toClient.ClientErrorMessage;
import it.polimi.ingsw.messages.toClient.ClientExitMessage;
import it.polimi.ingsw.messages.toClient.ClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The client-side class for socket connection.
 * This class is responsible for establishing and managing the socket connection with the server.
 * @author Gabriele Marra, Marco Mariotti, Andrea Gollo, Francesco Foresti
 */
public class ClientSocket {
    private String hostName;
    private int portNumber;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Socket clientSocket;
    private Controller controller;
    private InetSocketAddress socketAddress;
    private ClientMessage inMessage;
    private final Thread messageReceiver;
    private final Thread serverObserver;
    private BlockingQueue<ClientMessage> messageQueue;
    private MessageHandler messageHandler;

    /**
     * Constructs a new ClientSocket object.
     *
     * @param hostName   The hostname of the server.
     * @param portNumber The port number of the server.
     * @param controller The controller responsible for handling client messages.
     */
    public ClientSocket(String hostName, int portNumber, Controller controller) {
        this.controller = controller;
        this.portNumber = portNumber;
        this.hostName = hostName;
        socketAddress = new InetSocketAddress(this.hostName, this.portNumber);
        this.messageReceiver = new Thread(this::manageIncomingMessage);
        this.serverObserver = new Thread(this::waitMessage);
    }

    /**
     * Starts the socket connection.
     *
     * @throws IOException If an I/O error occurs during the connection.
     */
    public void startingPoint() throws IOException {
        this.messageQueue = new LinkedBlockingQueue<>();
        clientSocket = new Socket();

        try {
            clientSocket.connect(socketAddress, 3000);
            System.out.println("Connection successful!");
        } catch (IOException e) {
            controller.handleClientErrorMessage(new ClientErrorMessage(e.getMessage()));
            if (controller instanceof CLIcontroller) {
                ClientCLI clientCLI = new ClientCLI();
                clientCLI.start();
            }
        }

        os = new ObjectOutputStream(clientSocket.getOutputStream());
        is = new ObjectInputStream(clientSocket.getInputStream());
        if (!messageReceiver.isAlive()) messageReceiver.start();
        serverObserver.start();
        messageHandler = new MessageHandler(os);
    }

    /**
     * Returns the message handler associated with this client socket.
     *
     * @return The message handler.
     */
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    /**
     * Waits for incoming messages from the server.
     */
    public void waitMessage() {
        while (clientSocket.isConnected()) {
            try {
                inMessage = getClientMessage();
                if (inMessage instanceof ClientExitMessage) break;
                messageQueue.add(inMessage);
            } catch (Exception e) {
                System.err.println("FATAL ERROR");
                System.exit(255);
            }
        }
    }

    /**
     * Manages the incoming messages by passing them to the controller.
     */
    private void manageIncomingMessage() {
        while (clientSocket.isConnected()) {
            ClientMessage message;
            try {
                message = messageQueue.take();
            } catch (InterruptedException e) {
                try {
                    os.close();
                    is.close();
                    clientSocket.close();
                    messageReceiver.interrupt();
                    serverObserver.interrupt();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                return;
            }
            controller.handleMessage(message);
        }
    }

    /**
     * Reads a client message from the input stream.
     *
     * @return The client message received.
     */
    public ClientMessage getClientMessage() {
        ClientMessage message = null;
        try {
            message = (ClientMessage) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return message;
    }
}
