package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This abstract class represents a common goal strategy for the game.
 * It provides functionality for checking a library matrix,
 * managing a ranking of players that have achieved the common goal, and adding players to the list.
 *
 * @author Andrea Gollo, Marco Mariotti
 */
public abstract class CommonGoalStrategy implements Serializable {
    public ArrayList<String> rankingPlayer = new ArrayList<>();
    public int ID;

    /**
     * Checks the given library matrix based on the strategy's rules.
     *
     * @param libraryMatrix the matrix representing the game library
     * @return true if the strategy's condition is met, false otherwise
     */
    public abstract boolean check(Block[][] libraryMatrix);

    /**
     * Retrieves the ranking position of the specified player.
     *
     * @param p the player whose ranking position is to be retrieved
     * @return the ranking position of the player, or -1 if the player is not found in the ranking
     */
    public int getPosition(Player p) {
        if (rankingPlayer.contains(p.getNickName())) return rankingPlayer.indexOf(p.getNickName());
        return -1;
    }

    /**
     * Adds a player to the ranking list.
     *
     * @param p the player to be added to the ranking
     */
    public void addPlayer(Player p) {
        if ((rankingPlayer.size() < p.getMatch().getPlayers().size()) && (!rankingPlayer.contains(p.getNickName())))
            rankingPlayer.add(p.getNickName());
    }

    public int getID() {
        return ID;
    }
}

