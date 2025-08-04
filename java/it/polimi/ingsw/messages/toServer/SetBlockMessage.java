package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Block;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SetBlockMessage implements ServerMessage {
    public Block block;

    public SetBlockMessage(Block block) {
        this.block = block;
    }

    public Block getBlock(){
        return block;
    }

}