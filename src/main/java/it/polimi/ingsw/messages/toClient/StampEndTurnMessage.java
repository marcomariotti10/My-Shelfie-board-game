package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.server.model.Block;


public class StampEndTurnMessage  implements ClientMessage{

    private final Block[][] library;
    public StampEndTurnMessage(Block[][] matrix) {
        this.library = matrix;
    }
    public Block[][] getLibrary(){return this.library;}
}