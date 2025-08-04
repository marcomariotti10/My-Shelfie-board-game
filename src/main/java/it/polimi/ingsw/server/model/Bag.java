package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.ExceedNumberOfAvailableBlocksException;
import it.polimi.ingsw.exceptions.OutOfDrawRangeException;

import java.io.Serializable;
import java.util.Random;

/**
 *The Bag class represents a bag of blocks in a game.
 *It implements the Serializable interface to support object serialization.
 *@author Andrea Gollo, Gabriele Marra, Marco Mariotti
 */
public class Bag implements Serializable {
    private final int[] numberOfBlocks;
    private final Board board;

    /**
     *Constructs a Bag object with a specified game board.
     *@param board the game board to associate with the bag.
     */
    public Bag(Board board){
        this.board = board;
        numberOfBlocks = new int[]{22,22,22,22,22,22}; // Initialize the block counts
    }

    /**
     *Returns the number of blocks available for a specific block type.
     *@param block the block type to get the count for
     *@return the number of available blocks for the specified type
     */
    public int getBlockNumber(Block block){
        return numberOfBlocks[block.getIndex()];
    }

    /**
     *@return the total number of blocks in the bag
     */
    public int getTotalNumberOfBlocks(){
        int count = 0;
        for (int j : numberOfBlocks) {
            count += j;
        }
        return count;
    }

    /**
     *Draws a specified number of blocks from the bag.
     *@param numberOfBlocksToDraw the number of blocks to draw
     *@return an array of drawn blocks
     *@throws OutOfDrawRangeException if the specified number of blocks to draw is out of range
     *@throws ExceedNumberOfAvailableBlocksException if the specified number of blocks exceeds the total available blocks in the bag
     */
    public Block[] draw(int numberOfBlocksToDraw) throws OutOfDrawRangeException, ExceedNumberOfAvailableBlocksException {
        if(numberOfBlocksToDraw < 1 || numberOfBlocksToDraw >3) throw new OutOfDrawRangeException();
        if(numberOfBlocksToDraw > getTotalNumberOfBlocks()) throw new ExceedNumberOfAvailableBlocksException();

        Block[] drawnBlocks = new Block[numberOfBlocksToDraw];
        for(int i = 0; i<numberOfBlocksToDraw; i++){
            int randomIndex;
            do{
                randomIndex = new Random().nextInt(numberOfBlocks.length);
            } while(numberOfBlocks[randomIndex]<=0);
            drawnBlocks[i] = Block.values()[randomIndex];
            numberOfBlocks[randomIndex]--;
        }
        return drawnBlocks;
    }

    /**
     Adds a block to the bag.
     @param block The block to be added.
     @throws IllegalArgumentException if the provided block is Block.NULL.
     */
    public void addBlockToBag(Block block){
        if(block == Block.NULL) throw new IllegalArgumentException();
        numberOfBlocks[block.getIndex()] = getBlockNumber(block) + 1;
    }
}
