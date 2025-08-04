package it.polimi.ingsw.messages.toClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EndGameMessage  implements ClientMessage{
    public String winnerName;

    public String string;
    public EndGameMessage(String winnerName)  {
        this.winnerName = winnerName;
        string = "Warnings! Last Round! the player: " + winnerName + " filled all his/her Library!";
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getString() {
        return string;
    }
}
