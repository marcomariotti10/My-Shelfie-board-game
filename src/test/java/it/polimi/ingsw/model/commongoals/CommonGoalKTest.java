package it.polimi.ingsw.model.commongoals;


import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commongoals.CommonGoalK;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalKTest {
    private Block[][] matrixLibrary;
    @BeforeEach
    public void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {
        Library library = new Library(new Player("name", new Match(2,2)));
        matrixLibrary = library.getMatrix();
    }

    @Test
    void check1() throws ExceededLibraryLimitException {
        CommonGoalK c = new CommonGoalK();


        matrixLibrary[0][0] = Block.BLUE;       //correct
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.GREEN;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.GREEN;
        matrixLibrary[1][3] = Block.GREEN;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.GREEN;
        matrixLibrary[2][1] = Block.GREEN;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.GREEN;

        matrixLibrary[3][0] = Block.GREEN;
        matrixLibrary[3][1] = Block.GREEN;
        matrixLibrary[3][2] = Block.PINK;
        matrixLibrary[3][3] = Block.GREEN;
        matrixLibrary[3][4] = Block.GREEN;

        matrixLibrary[4][0] = Block.GREEN;
        matrixLibrary[4][1] = Block.BLUE;
        matrixLibrary[4][2] = Block.GREEN;
        matrixLibrary[4][3] = Block.GREEN;
        matrixLibrary[4][4] = Block.BLUE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.GREEN;
        matrixLibrary[5][2] = Block.GREEN;
        matrixLibrary[5][3] = Block.GREEN;
        matrixLibrary[5][4] = Block.GREEN;

        assertTrue(c.check(matrixLibrary));
    }

        @Test
        void check2() throws ExceededLibraryLimitException {
            CommonGoalK c = new CommonGoalK();
        matrixLibrary[0][0] = Block.BLUE;       //wrong
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.NULL;

        matrixLibrary[1][0] = Block.GREEN;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.GREEN;
        matrixLibrary[1][3] = Block.GREEN;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.GREEN;
        matrixLibrary[2][1] = Block.GREEN;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.GREEN;

        matrixLibrary[3][0] = Block.GREEN;
        matrixLibrary[3][1] = Block.GREEN;
        matrixLibrary[3][2] = Block.PINK;
        matrixLibrary[3][3] = Block.BLUE;
        matrixLibrary[3][4] = Block.GREEN;

        matrixLibrary[4][0] = Block.GREEN;
        matrixLibrary[4][1] = Block.BLUE;
        matrixLibrary[4][2] = Block.GREEN;
        matrixLibrary[4][3] = Block.GREEN;
        matrixLibrary[4][4] = Block.BLUE;

        matrixLibrary[5][0] = Block.BLUE;
        matrixLibrary[5][1] = Block.GREEN;
        matrixLibrary[5][2] = Block.GREEN;
        matrixLibrary[5][3] = Block.GREEN;
        matrixLibrary[5][4] = Block.GREEN;


        assertFalse(c.check(matrixLibrary));

    }
}
