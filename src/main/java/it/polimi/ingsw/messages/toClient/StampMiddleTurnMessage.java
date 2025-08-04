package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.server.model.Block;

import java.util.ArrayList;

public class StampMiddleTurnMessage implements ClientMessage{
    private final ArrayList<Block> availableBlocks;
    private final Block[][] library;
    public StampMiddleTurnMessage( ArrayList<Block> blocks, Block[][] matrix) {
        this.availableBlocks = blocks;
        this.library = matrix;
    }

    public ArrayList<Block> getAvailableBlocks() {
        return availableBlocks;
    }
    public Block[][] getLibrary() {
        return library;
    }
}
