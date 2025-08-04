package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.ClientHandlerSocket;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.controllers.TurnController;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

class LoginMessageTest {
/*
    private Client client1;
    @BeforeEach
    void setUp() throws InvalidNumberOfPlayersException, IOException {
        Server server = new Server(1234);
        server.startWebSocketServer();
        client1 = new Client("127.0.0.1",1234);
        client1.startListening();
        Client client2 = new Client("127.0.0.1",1234);
        client2.startListening();
        LobbyController lobbyController = new LobbyController("lobby",2,2);
        Match match = new Match(2,2,lobbyController);
        Player player = new Player("name",match);

        TurnController turnController = new TurnController(new ClientHandlerSocket(new Socket(),new Server(1234)),player, match);
    }

    @Test
    void handleMessage() {
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.handleMessage(client1);

    }

 */
}