package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.exceptions.NotEnoughSpaceException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Library library;
    private Block[][] matrixLibrary;
    private Player player;



    @BeforeEach
    public void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {

        library = new Library(new Player("giocatore",new Match(4, 2)));
        matrixLibrary = library.getMatrix();
        player = new Player("giocatore",new Match(4, 2));

    }

    @Test
    void checkLibrary(){
        Block[][] matrix = new Block[6][5];
        library = new Library(matrix);
        assertSame(library.getMatrix(), matrix);

    }

    @Test
    void checkAddBlock() throws ExceededLibraryLimitException {


        matrixLibrary[0][0] = Block.NULL;
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;

        matrixLibrary[3][0] = Block.BLUE;
        matrixLibrary[3][1] = Block.ORANGE;
        matrixLibrary[3][2] = Block.ORANGE;
        matrixLibrary[3][3] = Block.ORANGE;
        matrixLibrary[3][4] = Block.ORANGE;

        matrixLibrary[4][0] = Block.LIGHT_BLUE;
        matrixLibrary[4][1] = Block.WHITE;
        matrixLibrary[4][2] = Block.WHITE;
        matrixLibrary[4][3] = Block.WHITE;
        matrixLibrary[4][4] = Block.WHITE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.BLUE;
        matrixLibrary[5][2] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.BLUE;

        library.addBlock(0, Block.PINK);

        assertSame(matrixLibrary[0][0], Block.PINK);
    }

    @Test
    void checkSetPlayer(){
        library.setPlayer(player);
        assertSame(player, player);
    }


    @Test
    void checkAddable(){
        matrixLibrary[0][0] = Block.NULL;
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;

        matrixLibrary[3][0] = Block.BLUE;
        matrixLibrary[3][1] = Block.ORANGE;
        matrixLibrary[3][2] = Block.ORANGE;
        matrixLibrary[3][3] = Block.ORANGE;
        matrixLibrary[3][4] = Block.ORANGE;

        matrixLibrary[4][0] = Block.LIGHT_BLUE;
        matrixLibrary[4][1] = Block.WHITE;
        matrixLibrary[4][2] = Block.WHITE;
        matrixLibrary[4][3] = Block.WHITE;
        matrixLibrary[4][4] = Block.WHITE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.BLUE;
        matrixLibrary[5][2] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.BLUE;


        assertTrue(library.addable(0, 1));

    }
    @Test
    void checkEnoughSpace() throws NotEnoughSpaceException{
        matrixLibrary[0][0] = Block.NULL;
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;

        matrixLibrary[3][0] = Block.BLUE;
        matrixLibrary[3][1] = Block.ORANGE;
        matrixLibrary[3][2] = Block.ORANGE;
        matrixLibrary[3][3] = Block.ORANGE;
        matrixLibrary[3][4] = Block.ORANGE;

        matrixLibrary[4][0] = Block.LIGHT_BLUE;
        matrixLibrary[4][1] = Block.WHITE;
        matrixLibrary[4][2] = Block.WHITE;
        matrixLibrary[4][3] = Block.WHITE;
        matrixLibrary[4][4] = Block.WHITE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.BLUE;
        matrixLibrary[5][2] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.BLUE;

        assertFalse(library.enoughSpace(2));
        assertFalse(library.enoughSpace(5));
    }

    @Test
    void checkEnoughSpace1() throws NotEnoughSpaceException{
        matrixLibrary[0][0] = Block.NULL;
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.NULL;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.NULL;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;

        matrixLibrary[3][0] = Block.BLUE;
        matrixLibrary[3][1] = Block.ORANGE;
        matrixLibrary[3][2] = Block.ORANGE;
        matrixLibrary[3][3] = Block.ORANGE;
        matrixLibrary[3][4] = Block.ORANGE;

        matrixLibrary[4][0] = Block.LIGHT_BLUE;
        matrixLibrary[4][1] = Block.WHITE;
        matrixLibrary[4][2] = Block.WHITE;
        matrixLibrary[4][3] = Block.WHITE;
        matrixLibrary[4][4] = Block.WHITE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.BLUE;
        matrixLibrary[5][2] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.BLUE;

        assertTrue(library.enoughSpace(3));
    }

}