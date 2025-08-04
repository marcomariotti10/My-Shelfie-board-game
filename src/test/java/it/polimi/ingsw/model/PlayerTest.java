package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.Library;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PlayerTest {

    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;
    private Match match;

    @BeforeEach
    void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException, ExceedNumberOfPlayersException {
        this.match = new Match(4, 2);
        this.p1 = new Player("Luca", match);
        this.p2 = new Player("Marco", match);
        this.p3 = new Player("Giacomo",match);
        this.p4 = new Player("lorenzo",match);
        match.addPlayer(p1);
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.addPlayer(p4);
    }
    @Test
    void testgetPointsSecretGoalTest() throws ExceededLibraryLimitException {
        System.out.println(match.getPlayers());
        int[] secret = match.getSecretGoal(p1);
        Block[][] matrix = new Block[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = Block.LIGHT_BLUE;
            }
        }
        for (Block block: Block.values()){ //made the matrix to reach the max points for the secret goal
            if (!(block.getIndex()==-1)) matrix[secret[(block.getIndex())*2]][secret[(block.getIndex())*2+1]] = block;
        }
        p1.getLib().setMatrix(matrix);
        assertEquals(12, p1.getPointsSecretGoal());
        matrix[secret[0]][secret[1]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(9, p1.getPointsSecretGoal());
        matrix[secret[2]][secret[3]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(6, p1.getPointsSecretGoal());
        matrix[secret[4]][secret[5]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(4, p1.getPointsSecretGoal());
        matrix[secret[6]][secret[7]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(2, p1.getPointsSecretGoal());
        matrix[secret[8]][secret[9]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(1, p1.getPointsSecretGoal());
        matrix[secret[10]][secret[11]] = Block.NULL;
        p1.getLib().setMatrix(matrix);
        assertEquals(0, p1.getPointsSecretGoal());

    }

    @Test
    void testcheckEndGame() throws ExceededLibraryLimitException {
        for (int j = 0; j < 5; j++) {  //riempio la matrice con caselle non vuote (blu)
            for (int i = 0; i < 6; i++) {
                p1.getLib().addBlock(j, Block.BLUE);
            }
        }
        p1.checkEndGame();
        assertEquals(true,match.isEnded());
    }

    @Test
    void testgetPointsAdjacent() throws ExceededLibraryLimitException {
        Block[][] matrixLibrary = new Block[6][5];

        matrixLibrary[0][0] = Block.NULL;
        matrixLibrary[0][1] = Block.NULL;
        matrixLibrary[0][2] = Block.NULL;
        matrixLibrary[0][3] = Block.NULL;
        matrixLibrary[0][4] = Block.GREEN;

        matrixLibrary[1][0] = Block.NULL;
        matrixLibrary[1][1] = Block.BLUE;
        matrixLibrary[1][2] = Block.GREEN;
        matrixLibrary[1][3] = Block.GREEN;
        matrixLibrary[1][4] = Block.GREEN;

        matrixLibrary[2][0] = Block.BLUE;
        matrixLibrary[2][1] = Block.BLUE;
        matrixLibrary[2][2] = Block.BLUE;
        matrixLibrary[2][3] = Block.ORANGE;
        matrixLibrary[2][4] = Block.ORANGE;

        matrixLibrary[3][0] = Block.LIGHT_BLUE;
        matrixLibrary[3][1] = Block.LIGHT_BLUE;
        matrixLibrary[3][2] = Block.LIGHT_BLUE;
        matrixLibrary[3][3] = Block.ORANGE;
        matrixLibrary[3][4] = Block.ORANGE;

        matrixLibrary[4][0] = Block.LIGHT_BLUE;
        matrixLibrary[4][1] = Block.WHITE;
        matrixLibrary[4][2] = Block.WHITE;
        matrixLibrary[4][3] = Block.GREEN;
        matrixLibrary[4][4] = Block.ORANGE;

        matrixLibrary[5][0] = Block.LIGHT_BLUE;
        matrixLibrary[5][1] = Block.BLUE;
        matrixLibrary[5][2] = Block.BLUE;
        matrixLibrary[5][3] = Block.BLUE;
        matrixLibrary[5][4] = Block.ORANGE;

        p1.getLib().setMatrix(matrixLibrary);
        assertEquals(21, p1.getPointsAdjacent());
    }

    @Test
    public void testGetPointsFinal_WhenMatchNotEnded() {
        int actualPoints = p1.getPointsFinal();
        assertEquals(0, actualPoints);
    }

    @Test
    public void testGetPointsFinal_WhenPlayerIsLastPlayer() {
        match.gameEnded();
        match.setLastPlayer(p1);
        int actualPoints = p1.getPointsFinal();
        assertEquals(1, actualPoints);
    }

    @Test
    public void testGetPointsCommonGoal() {
        match.getCommonGoals().get(0).addPlayer(p1);
        match.getCommonGoals().get(1).addPlayer(p2);
        match.getCommonGoals().get(1).addPlayer(p3);
        match.getCommonGoals().get(1).addPlayer(p1);
        int expectedPoints = 8 + 4;
        int actualPoints = p1.getPointsCommonGoal();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testGetPoints() {
        int points = p1.getPoints();
        assertEquals(0, points);
    }

    @Test
    void testgetOthersPointsTest(){
        int points = p1.getOtherPoints();
        assertEquals(0,points);
    }

    @Test
    public void testGetMatch() {
        assertEquals(match, p1.getMatch());
    }

    @Test
    public void testConstructor() {
        String name = "Luca";
        int points = 0;
        Player player = new Player(name, match, match.getPlayers().get(0).getLib(), points);

        assertEquals(player.getNickName(), p1.getNickName());
        assertEquals(match, p1.getMatch());
        assertEquals(player.getLib(), p1.getLib());
        assertEquals(0, p1.getPoints());
    }
}



