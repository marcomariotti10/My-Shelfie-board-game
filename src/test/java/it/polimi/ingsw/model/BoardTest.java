package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Bag;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private Board board1;
    Block[][] matrix;
    Block[][] matrix1;
    Bag bag;

    @BeforeEach
    public void setUp() throws InvalidNumberOfPlayersException {
        matrix = new Block[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix[i][j] = Block.NULL;
            }
        }
        matrix1 = new Block[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix1[i][j] = Block.BLUE;
            }
        }
        final int nPlayers = 2;
        board = new Board(nPlayers);
        bag = new Bag(board);
        board1 = new Board(matrix, 2, bag);
    }


    @Test
    void getBagTest(){
        assertEquals(board1.getBag(), bag);
    }
    @Test
    void cunstroctorTest(){
        assertTrue(board1.getMatrix().equals(matrix));
        assertTrue(board1.getBag().equals(bag));
        assertTrue(board1.checkEmptyMatrix());
    }

    @Test
    void checkUpgraded(){
        board1.setUpgradeBoard(true);
        assertTrue(board1.isUpgradeBoard());
    }

    @Test
    public void takableTest() throws NotFreeBlockException, NotAdjacentException, NullBlockException, OutOfTakeRangeException, NotStraightException {
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));
        positions1.add(new ArrayList<>(Arrays.asList(1,4)));
        boolean takable = board.takable(positions1);
        assertTrue(takable);
    }

    @Test
    public void takableNullExceptionTest() {
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(0,0)));
        positions1.add(new ArrayList<>(Arrays.asList(0,1)));
        Exception exception = assertThrows(NullBlockException.class, () -> board.takable(positions1));
        assertEquals("Null block not allowed", exception.getMessage());
    }

    @Test
    public void takableAdjacentTest() throws NotFreeBlockException, NotAdjacentException, NullBlockException, OutOfTakeRangeException, NotStraightException {
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));
        positions1.add(new ArrayList<>(Arrays.asList(1,4)));
        assertTrue(board.takable(positions1));

    }
    @Test
    public void takableNotAdjacentTest(){
        //On different columns
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));
        positions1.add(new ArrayList<>(Arrays.asList(2,5)));
        //on same column
        Set<ArrayList<Integer>> positions2 = new HashSet<>();
        positions2.add(new ArrayList<>(Arrays.asList(1,3)));
        positions2.add(new ArrayList<>(Arrays.asList(7,5)));
        //on same row
        Set<ArrayList<Integer>> positions3 = new HashSet<>();
        positions3.add(new ArrayList<>(Arrays.asList(2,3)));
        positions3.add(new ArrayList<>(Arrays.asList(2,5)));
        assertThrows(NotAdjacentException.class, () -> board.takable(positions1));
        assertThrows(NotAdjacentException.class, () -> board.takable(positions2));
        assertThrows(NotAdjacentException.class, () -> board.takable(positions3));

    }

    @Test
    public void takableNotFreeTest(){
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(0,0)));
        positions1.add(new ArrayList<>(Arrays.asList(0,1)));
        assertThrows(NullBlockException.class, () -> board.takable(positions1));
    }

    @Test
    public void takableOutOfRangeExceptionTest(){
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(4,8)));
        positions1.add(new ArrayList<>(Arrays.asList(5,7)));
        positions1.add(new ArrayList<>(Arrays.asList(6,6)));
        positions1.add(new ArrayList<>(Arrays.asList(7,5)));
        Set<ArrayList<Integer>> positions2 = new HashSet<>();
        assertThrows(OutOfTakeRangeException.class, () -> board.takable(positions1));
        assertThrows(OutOfTakeRangeException.class, () -> board.takable(positions2));
    }

    @Test
    public void tackableNotStraight() throws NotFreeBlockException, NotAdjacentException, OutOfTakeRangeException, NullBlockException, NotStraightException {
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));
        positions1.add(new ArrayList<>(Arrays.asList(1,4)));
        positions1.add(new ArrayList<>(Arrays.asList(2,3)));
        assertThrows(NotStraightException.class, () -> board.takable(positions1));

        Set<ArrayList<Integer>> positions2 = new HashSet<>();
        positions2.add(new ArrayList<>(Arrays.asList(1,3)));
        positions2.add(new ArrayList<>(Arrays.asList(1,4)));
        assertTrue(board.takable(positions2));
    }


    @Test
    public void takeTest() throws NotFreeBlockException, NotAdjacentException, NullBlockException, OutOfTakeRangeException, NotStraightException {
        int expectedBlocksAfterRefill = 29;
        Block[][] matrixNull = new Block[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrixNull[i][j] = Block.NULL;
            }
        }

        matrixNull[1][3] = Block.BLUE;
        matrixNull[1][4] = Block.BLUE;

        Board nullBoard = new Board(matrixNull, 2, bag);
        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));
        positions1.add(new ArrayList<>(Arrays.asList(1,4)));

        nullBoard.take(positions1);

        int notNullBlocks = 0;
        for(Block[] row : nullBoard.getMatrix()){
            for(Block block : row){
                if(!block.equals(Block.NULL)){
                    notNullBlocks++;
                }
            }
        }

        assertEquals(expectedBlocksAfterRefill, notNullBlocks);
    }

    @Test
    public void fillBoardTestWithIsolatedBlocks() {
        int expectedBlocksAfterRefill = 29;
        Block[][] matrixNull = new Block[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrixNull[i][j] = Block.NULL;
            }
        }

        matrixNull[1][3] = Block.BLUE;
        matrixNull[1][4] = Block.BLUE;

        matrixNull[3][2] = Block.BLUE;
        matrixNull[3][3] = Block.BLUE;

        Board nullBoard = new Board(matrixNull, 2, bag);

        Set<ArrayList<Integer>> positions1 = new HashSet<>();
        positions1.add(new ArrayList<>(Arrays.asList(1,3)));

        nullBoard.take(positions1); //do not have to call refill

        Set<ArrayList<Integer>> positions2 = new HashSet<>();
        positions2.add(new ArrayList<>(Arrays.asList(3,2)));

        nullBoard.take(positions2);

        int notNullBlocks = 0;
        for(Block[] row : nullBoard.getMatrix()){
            for(Block block : row){
                if(!block.equals(Block.NULL)){
                    notNullBlocks++;
                }
            }
        }

        assertEquals(expectedBlocksAfterRefill, notNullBlocks);

    }

}
