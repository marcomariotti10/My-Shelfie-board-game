package it.polimi.ingsw.model.commongoals;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.commongoals.CommonGoalD;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalDTest {
    private Block[][] matrixLibrary;
    @BeforeEach
    public void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {
        Library library = new Library(new Player("name", new Match(2,2)));
        matrixLibrary = library.getMatrix();

    }

    @Test
    public void successCheckTest(){
        CommonGoalD c = new CommonGoalD();
        matrixLibrary[0][0] = Block.GREEN;
        matrixLibrary[1][0] = Block.GREEN;
        matrixLibrary[0][1] = Block.GREEN;
        matrixLibrary[1][1] = Block.GREEN;

        matrixLibrary[3][0] = Block.GREEN;
        matrixLibrary[4][0] = Block.GREEN;
        matrixLibrary[3][1] = Block.GREEN;
        matrixLibrary[4][1] = Block.GREEN;

        assertTrue(c.check(matrixLibrary));
    }

    @Test
    public void failCheckTest(){
        CommonGoalD c = new CommonGoalD();
        matrixLibrary[0][0] = Block.GREEN;
        matrixLibrary[1][0] = Block.GREEN;
        matrixLibrary[0][1] = Block.GREEN;
        matrixLibrary[1][1] = Block.GREEN;

        matrixLibrary[3][0] = Block.GREEN;
        matrixLibrary[4][0] = Block.GREEN;
        matrixLibrary[3][1] = Block.GREEN;
        matrixLibrary[4][1] = Block.GREEN;
        matrixLibrary[4][2] = Block.GREEN;

        assertFalse(c.check(matrixLibrary));
    }
}
