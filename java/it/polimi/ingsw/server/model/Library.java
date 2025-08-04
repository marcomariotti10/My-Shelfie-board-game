package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import java.io.Serializable;
import java.util.Arrays;

/**
 *Represents a library in the game.
 *The library holds a matrix of blocks and a player.
 *@author Andrea Gollo, Gabriele Marra, Francesco Foresti, Marco Mariotti
 */
public class Library implements Serializable {
    private Block[][] matrix;
    private Player player;

    /**
     *Constructs a new Library object with the given player.
     *Initializes the matrix with null blocks.
     *@param player the player associated with the library
     */
    public Library(Player player) {
        this.player = player;
        this.matrix = new Block[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = Block.NULL;
            }
        }
    }

    public Library(Block[][] matrix){
        this.matrix = matrix;
        this.player = null;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Block[][] getMatrix() {
        return matrix;
    }

     /**
      *Adds a new block to the library at the specified column.
      *@param column the column index to add the block
      *@param b the block to add
      *@throws ExceededLibraryLimitException if the library is full
     */
    public void addBlock(int column, Block b) throws ExceededLibraryLimitException {
        int i = 5;
        while (i >= 0) {
            if (matrix[i][column].getIndex() == -1) {
                this.matrix[i][column] = b;
                return;
            } else {
                i--;
            }
            if (i == -1) {
                throw new ExceededLibraryLimitException();
            }
        }
    }

    /**
     *Retrieves the block at the specified position in the library.
     *@param x the row index of the block
     *@param y the column index of the block
     *@return the block at the specified position
     */
    public Block getBlock(int x, int y) {
        return this.matrix[x][y];
    }

    /**
     *Creates a copy of the index of the given matrix.
     *@param matrix the matrix to copy
     *@return a copy of the matrix
     */
    public int[][] copyMatrix(Block[][] matrix) {
        int[][] copy = new int[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                copy[i][j] = matrix[i][j].getIndex();
            }
        }
        return copy;
    }

    /**
     *Sets the matrix of blocks in the library.
     *Copies a realistic matrix into the player's library.
     *@param matrix the matrix to set
     *@throws ExceededLibraryLimitException if the library is full
     */
    public void setMatrix(Block[][] matrix) throws ExceededLibraryLimitException {
        for(int i = 0; i<matrix.length; i++){
            for(int j = 0; j<matrix[0].length; j++){
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    /**
     *Checks if a set of blocks of the specified size can be added to the library at the given column.
     *@param column the column index to check
     *@param size the size of the block to add
     *@return true if the block can be added, false otherwise
     */
    public boolean addable(int column, int size) {
        if (column < 0 || column > 4 || size < 0 || size > 3) {
            return Boolean.FALSE;
        }
        return this.matrix[size - 1][column] == Block.NULL;
    }

    /**
     *Checks if there is enough space in the library to add a block of the specified size.
     *@param size the size of the block to add
     *@return true if there is enough space, false otherwise
     *@throws NotEnoughSpaceException if there is not enough space in the library
     */
    public boolean enoughSpace(int size) throws NotEnoughSpaceException {
        int i;
        if (size < 1 || size > 3) {
            return Boolean.FALSE;
        } else {
            for (i = 0; i < 5; i++) {
                if (this.matrix[size - 1][i] == Block.NULL) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }
    }
}