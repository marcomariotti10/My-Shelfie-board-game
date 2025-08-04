package it.polimi.ingsw.server;
import it.polimi.ingsw.messages.toClient.ClientMessage;
import it.polimi.ingsw.messages.toClient.LoginMessage;
import it.polimi.ingsw.messages.toServer.ServerMessage;
import it.polimi.ingsw.server.model.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 *The ClientHandlerSocket class extends the ClientHandler class and implements the Runnable and Serializable interfaces.
 *It handles communication with a client using sockets.
 *@author Gabriele Marra, Marco Mariotti
 */
public class ClientHandlerSocket extends ClientHandler implements Runnable, Serializable {
    private final Socket clientSocket;
    private final ObjectOutputStream os;
    private final ObjectInputStream is;

    /**
     *Constructs a ClientHandlerSocket object with the specified socket and server.
     *@param socket the socket representing the client connection
     *@param server the server instance associated with the client handler
     *@throws IOException if an I/O error occurs while creating the object streams
     */
    public ClientHandlerSocket(Socket socket, Server server) throws IOException{
        super(server);
        this.clientSocket = socket;
        os = new ObjectOutputStream(clientSocket.getOutputStream());
        is = new ObjectInputStream(clientSocket.getInputStream());
    }

    /**
     *Runs the client handler thread. It sends a LoginMessage to the client and continuously receives and handles messages from the client.
     */
    public void run() {
        sendMessage(new LoginMessage());
        ServerMessage recivedMessage;
        while (true) {
            recivedMessage = getMessage();
            handleMessage(recivedMessage);
        }
    }

    /**
     *Sends a ClientMessage to the client.
     *@param message the ClientMessage to be sent
     */
    @Override
    public void sendMessage(ClientMessage message) {
        try {
            os.writeObject(message);
            os.flush();
            os.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Receives a ServerMessage from the client.
     *@return the received ServerMessage
     */
    public ServerMessage getMessage() {
        try {
            return (ServerMessage) is.readObject();
        } catch (Exception e) {
            getServer().removeClientHandler(this);
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     *Retrieves the player associated with this client handler from the lobby controller.
     *@return the player object associated with this client handler, or null if the match is not set
     */
    public Player getPlayer(){
        if(getLobbyController().getMatch() != null) return getLobbyController().getPlayer(this);
        return null;
    }
}