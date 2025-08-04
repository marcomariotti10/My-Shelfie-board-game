package it.polimi.ingsw.messages.toClient;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StampEndGameMessage implements ClientMessage {
    private  int[][] points;
    private String[] nickNames;

    public StampEndGameMessage(int[][] points, String[] nickNames) {
        this.nickNames = nickNames;
        this.points = points;
    }

    public int[][] getPoints() {
        return points;
    }

    public String[] getNickNames() {
        return nickNames;
    }

}