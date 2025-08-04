package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commongoals.CommonGoalF;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalFTest {
    private Block[][] matrixLibrary;
    @BeforeEach
    public void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {
        Library library = new Library(new Player("name", new Match(2,2)));
        matrixLibrary = library.getMatrix();
    }

    @Test
    public void successCheckTest(){
        CommonGoalF c = new CommonGoalF();
        matrixLibrary[0][0] = Block.BLUE;
        matrixLibrary[1][0] = Block.BLUE;
        matrixLibrary[3][0] = Block.BLUE;
        matrixLibrary[4][0] = Block.BLUE;
        matrixLibrary[0][1] = Block.BLUE;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[3][1] = Block.BLUE;
        matrixLibrary[4][1] = Block.BLUE;
        matrixLibrary[3][2] = Block.BLUE;
        matrixLibrary[4][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;
        matrixLibrary[3][3] = Block.BLUE;
        matrixLibrary[4][4] = Block.BLUE;

        assertTrue(c.check(matrixLibrary));
    }

    @Test
    public void failCheckTest(){
        CommonGoalF c = new CommonGoalF();
        matrixLibrary[3][1] = Block.BLUE;
        matrixLibrary[4][1] = Block.BLUE;
        matrixLibrary[3][2] = Block.BLUE;
        matrixLibrary[4][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.BLUE;
        matrixLibrary[2][4] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.GREEN;

        assertFalse(c.check(matrixLibrary));
    }
}
