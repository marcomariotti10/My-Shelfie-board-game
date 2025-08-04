package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 *Represents the game board with blocks arranged in a matrix.
 *@author Andrea Gollo, Gabriele Marra, Francesco Foresti, Marco Mariotti
 */
public class Board implements Serializable {
    private Block[][] matrix;
    private final Bag bag;
    private final int numberOfPlayers;
    private boolean upgradeBoard = false;


    /**
     *Constructs a Board object with the specified number of players and match.
     *@param numberOfPlayers the number of players participating in the game
     *@param @match the match in which the board is used
     *@throws InvalidNumberOfPlayersException if the number of players is invalid
     */
    public Board(int numberOfPlayers) throws InvalidNumberOfPlayersException {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) throw new InvalidNumberOfPlayersException();
        this.numberOfPlayers = numberOfPlayers;
        this.matrix = new Block[9][9];
        this.bag = new Bag(this);
        for (Block[] row : matrix)
            Arrays.fill(row, Block.NULL);
        fillMatrix();
    }

    public Board(Block[][] matrix, int numberOfPlayers, Bag bag){
        this.matrix = matrix;
        this.numberOfPlayers = numberOfPlayers;
        this.bag = bag;
    }

    /**
     *Fills a specific row of the matrix with blocks within the given range
     * using the method fill(int row, int column).
     *@param row the row index of the matrix
     *@param start the starting column index for filling
     *@param finish the ending column index for filling
     */
    private void fillRow(int row, int start, int finish) {
        for (int i = start; i <= finish; i++) {
            fill(row, i);
        }
    }

    /**
     *Fills a specific cell in the matrix with a block drawn from the bag.
     *If the bag is empty, a null block is placed instead.
     *@param row the row index of the matrix
     *@param column the column index of the matrix
     */
    private void fill(int row, int column) {
        try {
            matrix[row][column] = bag.draw(1)[0];
        } catch (OutOfDrawRangeException e) {
            throw new RuntimeException(e);
        } catch (ExceedNumberOfAvailableBlocksException e) {
            //If we have to fill the board and blocks are finished
            matrix[row][column] = Block.NULL;
        }
    }

    /**
     *Fills the entire matrix with blocks according to the game instructions.
     */
    private void fillMatrix(){
        //Fill each row for 2 players with not null blocks according to game instruction
        fillRow(1, 3, 4);
        fillRow(2, 3, 5);
        fillRow(3, 2, 7);
        fillRow(4, 1, 7);
        fillRow(5, 1, 6);
        fillRow(6, 3, 5);
        fillRow(7, 4, 5);
        //Add blocks for 3 players with not null blocks according to game instruction
        if (numberOfPlayers >= 3) {
            fill(0, 3);
            fill(2, 2);
            fill(2, 6);
            fill(3, 8);
            fill(5, 0);
            fill(6, 2);
            fill(6, 6);
            fill(8, 5);
        }
        //Add blocks for 4 players with not null blocks according to game instruction
        if (numberOfPlayers == 4) {
            fill(0, 4);
            fill(1, 5);
            fill(3, 1);
            fill(4, 0);
            fill(4, 8);
            fill(5, 7);
            fill(7, 3);
            fill(8, 4);
        }
    }

    /**
     *Checks if two positions in the matrix are adjacent to each other.
     *@param position1 the first position as an ArrayList of row and column indices
     *@param position2 the second position as an ArrayList of row and column indices
     *@return true if the positions are adjacent even in diagonal, false otherwise
     */
    private boolean areAdjacent(ArrayList<Integer> position1, ArrayList<Integer> position2){
        int xOffset = Math.abs(position1.get(0) - position2.get(0));
        int yOffset = Math.abs(position1.get(1) - position2.get(1));
        return (xOffset == 1 && yOffset == 0) || (xOffset == 0 && yOffset == 1);
    }

    /**
     * Checks whether a set of positions is in a straight line either vertically or horizontally.
     *
     * @param positions the set of positions represented as {@code ArrayList<Integer>} where index 0 represents the x-coordinate and index 1 represents the y-coordinate.
     * @return {@code true} if all positions are in a straight line vertically or horizontally, {@code false} otherwise.
     */
    public static boolean arePositionsInLine(Set<ArrayList<Integer>> positions) {
        boolean isVertical = true;
        boolean isHorizontal = true;

        Integer prevX = null;
        Integer prevY = null;
        for(ArrayList<Integer> position : positions){
            Integer currentX = position.get(0);
            Integer currentY = position.get(1);

            if(prevX == null && prevY == null){//if first iteration
                prevX = currentX;
                prevY = currentY;
            } else {
                if(isVertical){
                    if(!prevX.equals(currentX)){
                        isVertical = false;
                    }
                }
                if(isHorizontal){
                    if(!prevY.equals(currentY)){
                        isHorizontal = false;
                    }
                }
            }
        }

        return isVertical || isHorizontal;
    }

    /**
     *Checks if a set of blocks in certain positions on the board can be taken by a player.
     *Throws exceptions if any of the blocks are invalid or not adjacent.
     *@param positions a set of positions to be taken as ArrayLists of row and column indices
     *@return true if the positions can be taken, false otherwise
     *@throws NotFreeBlockException if any of the blocks are not free
     *@throws NullBlockException if any of the blocks are null
     *@throws NotAdjacentException if any of the blocks are not adjacent to each other
     *@throws OutOfTakeRangeException if the number of positions is less than 1 or greater than 3
     *@throws NotStraightException if the blocks are not in line
     */
    public boolean takable(Set<ArrayList<Integer>> positions) throws NotFreeBlockException, NullBlockException, NotAdjacentException, OutOfTakeRangeException, NotStraightException {
        if(positions.size() < 1 || positions.size() > 3) throw new OutOfTakeRangeException();
        for (ArrayList<Integer> position1 : positions) {
            //check if all blocks are not null
            if(matrix[position1.get(0)][position1.get(1)] == Block.NULL) throw new NullBlockException();
            //check if all blocks are free
            if (position1.get(0) != 0 && position1.get(0) != 8 && position1.get(1) != 0 && position1.get(1) != 8) {
                if(matrix[position1.get(0) + 1][position1.get(1)] != Block.NULL && matrix[position1.get(0) - 1][position1.get(1)] != Block.NULL &&
                        matrix[position1.get(0)][position1.get(1) + 1] != Block.NULL && matrix[position1.get(0)][position1.get(1) - 1] != Block.NULL)
                    throw new NotFreeBlockException();
            }
            if(positions.size() > 1){
                boolean adjacentPresent = false;
                for (ArrayList<Integer> position2 : positions) {
                    //check if all blocks are adjacent, if they have the same positions they are also not adjacent
                    if (!position1.equals(position2) && areAdjacent(position1, position2)) {
                        adjacentPresent = true;
                        break;
                    }
                }
                if (!adjacentPresent) {
                    throw new NotAdjacentException();
                }
                if(!arePositionsInLine(positions)){
                    throw new NotStraightException();
                }
            }
        }
        return true;
    }

    /**
     *Takes the blocks at the specified positions on the board.
     *Removes the blocks from the matrix and returns them in an ArrayList.
     *Refills the matrix if it becomes empty after taking the blocks using fillMatrix().
     *Refills the matrix if all remaining blocks are isolated, add them to the bag again and call fillMatrix()
     *@param positions a set of positions to be taken as ArrayLists of row and column indices
     *@return an ArrayList of the blocks that were taken
     */
    public ArrayList<Block> take(Set<ArrayList<Integer>> positions){
        ArrayList<Block> drawedBlock = new ArrayList<>();
        for (ArrayList<Integer> position : positions){
            drawedBlock.add(matrix[position.get(0)][position.get(1)]);
            matrix[position.get(0)][position.get(1)] = Block.NULL;
        }
        //Refill the matrix if empty
        if(checkEmptyMatrix()){
            fillMatrix();
            upgradeBoard=true;
        }

        if(areAllBlocksIsolated()){
            for (Block[] blocks : matrix) {
                for (Block block : blocks) {
                    if (block != Block.NULL) {
                        bag.addBlockToBag(block);
                    }
                }
            }
            fillMatrix();
            upgradeBoard=true;

        }
        return drawedBlock;
    }

    /**
     * Checks if all not-null blocks in the matrix are isolated.
     * @return true if all not-null blocks are isolated, false otherwise
     */
    private boolean areAllBlocksIsolated() {
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                if (!isIsolateBlock(row, column) && !matrix[row][column].equals(Block.NULL)) {
                    System.out.println(isIsolateBlock(row, column));
                    return false;  // return false if at least one block is not isolated
                }
            }
        }
        return true;  // all blocks are isolated
    }

    /**
     * Checks if the block at the specified row and column is isolated.
     *
     * @param row    the row index of the block
     * @param column the column index of the block
     * @return true if the block is isolated, false otherwise
     */
    private boolean isIsolateBlock(int row, int column) {
        int numRows = matrix.length;
        int numColumns = matrix[0].length;

        boolean isLeftNull = (column == 0 || matrix[row][column - 1].equals(Block.NULL));
        boolean isRightNull = (column == numColumns - 1 || matrix[row][column + 1].equals(Block.NULL));
        boolean isTopNull = (row == 0 || matrix[row - 1][column].equals(Block.NULL));
        boolean isBottomNull = (row == numRows - 1 || matrix[row + 1][column].equals(Block.NULL));

        return isLeftNull && isRightNull && isTopNull && isBottomNull;
    }


    /**
     * Check if the matrix is completely empty.
     * @return true if it's empty, false otherwise
     */
    public boolean checkEmptyMatrix(){
        for (Block[] row : matrix)
            for(Block block : row){
                if(block != Block.NULL) return false;
            }
        return true;
    }

    public Block[][] getMatrix() {
        return matrix;
    }

    public Bag getBag() {
        return bag;
    }

    public boolean isUpgradeBoard() {
        return upgradeBoard;
    }
    public void setUpgradeBoard(boolean upgradeBoard) {
        this.upgradeBoard = upgradeBoard;
    }
}
