package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.exceptions.ExceededLibraryLimitException;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Match match;

    @BeforeEach
    void setUp() throws ExceededLibraryLimitException, InvalidNumberOfPlayersException {
        ArrayList<String> nikNames = new ArrayList<>();
        nikNames.add("Marco");
        nikNames.add("Andrea");
        nikNames.add("Francesco");
        nikNames.add("Gabriele");
        //match = new Match(nikNames, 2);
        ArrayList<Player> players;
        players = new ArrayList<>();
        /*for(String n : nikNames){
            players.add(new Player("testPlayer", new Match()));
        }*/
        match = new Match(4, 2);
    }


    @Test
    void checkGame(){
        match.game(new Player("Gigi", match));
    }
    @Test
    Integer obtainSecretGoal() {
        boolean f=true;
        for (Player p: match.getPlayers() ) {
            if(!(Arrays.equals(match.getSecretGoal(p), new int[]{5, 5, 1, 3, 6, 1, 5, 2, 4, 5, 3, 3}) ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{5, 5, 6, 1, 4, 2, 2, 2, 4, 3, 6, 4}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{6, 1, 5, 2, 5, 4, 1, 3, 3, 4, 1, 5}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{3, 3, 2, 4, 2, 1, 4, 5, 6, 1, 4, 2}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{3, 2, 5, 5, 2, 4, 4, 1, 6, 3, 1, 1}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{2, 2, 5, 5, 3, 3, 4, 4, 1, 3, 6, 1}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{4, 4, 1, 5, 3, 3, 3, 1, 5, 2, 5, 3}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{1, 3, 3, 1, 4, 3, 6, 4, 2, 2, 5, 5}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{6, 4, 2, 2, 5, 2, 1, 5, 3, 1, 4, 4}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{4, 1, 6, 4, 1, 5, 3, 3, 5, 4, 2, 2}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{1, 1, 4, 2, 1, 3, 6, 3, 3, 4, 2, 5}))  ||
                    (Arrays.equals(match.getSecretGoal(p), new int[]{2, 2, 3, 3, 6, 5, 5, 4, 4, 5, 3, 1})))) f = false;
        }
        assertTrue(f);
        return null;
    }

    @Test
    void getCommonGoals() {
        assertEquals(2,match.getCommonGoals().size());
    }

    @Test
    void testSetBoard() throws InvalidNumberOfPlayersException {
        Board board = new Board(4);
        match.setBoard(board);
        assertEquals(board, match.getBoard());
    }

    @Test
    void testRestorePlayer() throws InvalidNumberOfPlayersException {
        ArrayList<Player> players = new ArrayList<>();
        Match match1 = new Match(4,2 );
        Player player1 = new Player("Player1", match1);
        Player player2 = new Player("Player2", match1);
        players.add(player1);
        players.add(player2);

        match.setPlayers(players);

        Player newPlayer1 = new Player("Player1", match1);
        match.restorePlayer(newPlayer1);

        assertEquals(newPlayer1, match.getPlayers().get(0));
    }

    @Test
    void testSetPlayers() throws InvalidNumberOfPlayersException {
        Match match1 = new Match(4,2 );
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player("Player1", match1);
        Player player2 = new Player("Player2", match1);
        players.add(player1);
        players.add(player2);

        match.setPlayers(players);
        assertEquals(players, match.getPlayers());
    }

    @Test
    void testGameEnded() {
        match.gameEnded();
        assertTrue(match.isEnded());
    }

    @Test
    void testGetNumberOfPlayers() {
        assertEquals(4, match.getNumberOfPlayers());
    }
    @Test
    void testIsEnded() {
        assertFalse(match.isEnded());
    }

    @Test
    void testAddPlayer() throws InvalidNumberOfPlayersException {
        Match match1 = new Match(4,2 );
        Player player = new Player("Player1", match1);
        try {
            match.addPlayer(player);
            // Inserire qui le asserzioni per verificare il corretto stato dell'oggetto Match dopo l'aggiunta del giocatore
        } catch (ExceedNumberOfPlayersException e) {
            fail("Unexpected ExceedNumberOfPlayersException");
        }
    }
}