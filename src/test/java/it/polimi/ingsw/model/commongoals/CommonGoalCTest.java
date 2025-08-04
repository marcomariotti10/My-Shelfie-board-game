package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commongoals.CommonGoalC;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCTest {
    private Block[][] matrixLibrary;
    @BeforeEach
    public void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {
        Library library = new Library(new Player("name", new Match(2,2)));
        matrixLibrary = library.getMatrix();
    }

    @Test
    void checkTrue() throws ExceededLibraryLimitException {  // check attraverso ranking attribute
        CommonGoalC c = new CommonGoalC();

        matrixLibrary[0][0] = Block.BLUE;
        matrixLibrary[0][1] = Block.BLUE;
        matrixLibrary[0][2] = Block.BLUE;
        matrixLibrary[0][3] = Block.BLUE;
        matrixLibrary[0][4] = Block.BLUE;

        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.LIGHT_BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.ORANGE;

        matrixLibrary[3][0] = Block.LIGHT_BLUE;
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

        assertTrue(c.check(matrixLibrary));
    }
    @Test
    void checkFalse() throws ExceededLibraryLimitException {  // check attraverso ranking attribute
        CommonGoalC c = new CommonGoalC();

        matrixLibrary[0][0] = Block.LIGHT_BLUE;
        matrixLibrary[0][1] = Block.BLUE;
        matrixLibrary[0][2] = Block.BLUE;
        matrixLibrary[0][3] = Block.BLUE;
        matrixLibrary[0][4] = Block.BLUE;

        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.BLUE;
        matrixLibrary[1][3] = Block.BLUE;
        matrixLibrary[1][4] = Block.BLUE;

        matrixLibrary[2][0] = Block.LIGHT_BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.ORANGE;

        matrixLibrary[3][0] = Block.LIGHT_BLUE;
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

        assertFalse(c.check(matrixLibrary));
    }

}