package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Bag;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.exceptions.ExceedNumberOfAvailableBlocksException;
import it.polimi.ingsw.exceptions.OutOfDrawRangeException;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Match;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BagTest {
    private Bag bag;
    @BeforeEach
    public void setUp() throws InvalidNumberOfPlayersException {
        bag = new Bag(new Board(4));
    }

    @Test
    public void decreaseAvailableBlockNumber() throws ExceedNumberOfAvailableBlocksException, OutOfDrawRangeException {
        int initialCount = bag.getTotalNumberOfBlocks();
        assertEquals(132, initialCount, "Initial Count");
        int blocksToDraw = 3;
        for(int i = 0; i<initialCount/blocksToDraw; i++){
            int currentCount = bag.getTotalNumberOfBlocks();
            Block[] drawn = bag.draw(blocksToDraw);
            assertEquals(blocksToDraw, drawn.length, "Drawn Size");
            assertEquals(currentCount-blocksToDraw, bag.getTotalNumberOfBlocks(), "Remaining blocks");
        }
        for(Block block : Block.values()){
            if(block.equals(Block.NULL)) break;
            assertEquals(0, bag.getBlockNumber(block));
        }
    }
    @Test
    public void testAddBlockToBag() {
        Block block = Block.BLUE;

        // Check if the number of blocks of the added block has increased by 1
        int expectedCount = bag.getBlockNumber(block) + 1;
        int actualCount = bag.getBlockNumber(block);

        // Add a block to the bag
        bag.addBlockToBag(block);

        assertEquals(expectedCount, actualCount + 1);

        // Try adding a null block, should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> bag.addBlockToBag(Block.NULL));
    }
}
