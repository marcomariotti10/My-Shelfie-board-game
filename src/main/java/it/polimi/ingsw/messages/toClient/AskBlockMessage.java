package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.messages.toServer.SetBlockMessage;
import it.polimi.ingsw.server.model.Block;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;


public class AskBlockMessage implements ClientMessage{
    private final ArrayList<Block> colors;

    public AskBlockMessage(ArrayList<Block> blocks) {
        this.colors = blocks;
    }

    public ArrayList<Block> getColors(){
        return this.colors;
    }

}