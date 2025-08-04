package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.SecretGoals;
import it.polimi.ingsw.exceptions.ExceedNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.server.model.commongoals.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *Represents a match in the game.
 *A match consists of players, a game board, common goals, secret goals, and other game-related information.
 *@author Andrea Gollo, Gabriele Marra, Marco Mariotti
 */
public class Match implements Serializable {
    final Integer NUMSECRETGOAL = 12;
    final Integer NUMCOMMONGOAL = 12;
    private ArrayList<Player> players = new ArrayList<>();
    private Board board;
    public  ArrayList<CommonGoalStrategy> commonGoals;
    private boolean ended;
    private Player lastPlayer;
    private final ArrayList<int []> secretGoals;
    private final int numberOfPlayers;

    /**
     *Starts the game for the specified player.
     *Checks if the player has finished his library.
     *@param player The player whose turn is being played.
     */
    public void game(Player player){
        if (!ended) {
            player.checkEndGame();
            }
        }

    /**
     *Constructs a new match with the specified number of players, number of common goals, and lobby controller.
     *@param numberOfPlayers The number of players in the match.
     *@param NumCG The number of common goals in the match.
     *@throws InvalidNumberOfPlayersException if the number of players is invalid.
     */
    public Match(int numberOfPlayers, int NumCG) throws InvalidNumberOfPlayersException {
        ended = false;
        this.numberOfPlayers = numberOfPlayers;
        this.board = new Board(numberOfPlayers);
        commonGoals = setCommonGoals(NumCG);
        secretGoals = setSecretGoals(numberOfPlayers); // se no players.size() è ancora a 0
    }

    /**
     *Adds a player to the match.
     *@param player The player to add.
     *@throws ExceedNumberOfPlayersException if the maximum number of players has been exceeded.
     */
    public void addPlayer(Player player) throws ExceedNumberOfPlayersException {
        if(players.size() < numberOfPlayers) players.add(player);
        else throw new ExceedNumberOfPlayersException();
    }

    /**
     *Sets the common goals for the match based on the specified number of common goals.
     *@param NumCG The number of common goals to set.
     *@return The list of common goals.
     */

    private ArrayList<CommonGoalStrategy> setCommonGoals(int NumCG){
        ArrayList<CommonGoalStrategy> t = new ArrayList<>();
        ArrayList<Integer> numCommonGoals = new ArrayList<>();
        numCommonGoals.add(new Random().nextInt(NUMCOMMONGOAL));
        while (NumCG>numCommonGoals.size()){
            numCommonGoals.add(new Random().nextInt(NUMCOMMONGOAL));
            if(numCommonGoals.get(0).equals(numCommonGoals.get(1))) numCommonGoals.remove(1);
        }
        for (Integer i: numCommonGoals) {
            switch (i) {
                case 0 -> t.add(new CommonGoalA());
                case 1 -> t.add(new CommonGoalB());
                case 2 -> t.add(new CommonGoalC());
                case 3 -> t.add(new CommonGoalD());
                case 4 -> t.add(new CommonGoalE());
                case 5 -> t.add(new CommonGoalF());
                case 6 -> t.add(new CommonGoalG());
                case 7 -> t.add(new CommonGoalH());
                case 8 -> t.add(new CommonGoalI());
                case 9 -> t.add(new CommonGoalJ());
                case 10 -> t.add(new CommonGoalK());
                case 11 -> t.add(new CommonGoalL());
            }
        }
        return t;
    }

    /**
     *Sets the secret goals for the match based on the number of players
     *using a random generator.
     *@param n The number of players in the match.
     *@return The list of secret goals.
     */
    private ArrayList<int []> setSecretGoals(int n){
        ArrayList<Integer> numSecretGoals = new ArrayList<>();
        ArrayList<int []> t = new ArrayList<>();
        while (numSecretGoals.size()<n) {
            Integer rand = new Random().nextInt(NUMSECRETGOAL);
            if (!numSecretGoals.contains(rand)) {
                numSecretGoals.add(rand);
            }
        }
        for (Integer i: numSecretGoals) {
            t.add(SecretGoals.getSG(i).getCoordinates());
        }
        return t;
    }
    public int[] getSecretGoal(Player player){
        return secretGoals.get(players.indexOf(player));
    }
    public Board getBoard(){
        return board;
    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void gameEnded() {ended = true;}

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setLastPlayer(Player lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public Player getLastPlayer() {
        return lastPlayer;
    }
    public void setPlayers(ArrayList<Player> players){
        System.out.println("siamo in setplayers");
        this.players = players;
    }

    public void restorePlayer(Player initPlayer){
        int index = 0;
        while (index<players.size()){
            if(players.get(index).getNickName().equals(initPlayer.getNickName())) break;
            index++;
        }
        this.players.set(index, initPlayer);
    }
    public void setBoard(Board board){
        System.out.println("siamo in setboard");
        this.board = board;
    }

    public ArrayList<CommonGoalStrategy> getCommonGoals(){
        return commonGoals;
    }

}
