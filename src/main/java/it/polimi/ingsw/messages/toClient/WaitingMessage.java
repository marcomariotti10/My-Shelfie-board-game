package it.polimi.ingsw.messages.toClient;

public class WaitingMessage implements ClientMessage{
    final String message = "Wait for the other players";

    public String getMessage() {
        return message;
    }
}
