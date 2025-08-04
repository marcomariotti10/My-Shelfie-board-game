package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *Represents a player in a game.
 *The player has a nickname, a library of game blocks, points, and belongs to a specific match.
 *@author Andrea Gollo,Gabriele Marra,Francesco Foresti,Marco Mariotti
 */
public class Player implements Serializable {

    private final String NickName;
    public Library Lib;
    private int points;
    private final Match match;

    /**
     *Constructs a new player with the specified nickname and match.
     *@param name The nickname of the player.
     *@param match The match the player is participating in.
     */
    public Player (String name, Match match) {
        this.NickName = name;
        this.match = match;
        Lib = new Library(this);
        this.points = 0;
    }

    public Player(String name, Match match, Library library, int points){
        this.NickName = name;
        this.match = match;
        this.Lib = library;
        this.points = points;
    }

    /**
     *Returns the points earned by the player for finishing first the match.
     *If the match is not ended or the player is not the last player, the points are zero.
     *@return The points earned for finishing first the match.
     */
    public int getPointsFinal() {
        if (!(match.isEnded())) return 0;
        else if (match.getLastPlayer().equals(this)) return 1;
        else return 0;
    }

    /**
     *Returns the points earned by the player for achieving the common goals in the match.
     *The points are calculated based on the positions achieved by the player in each common goal.
     *@return The points earned for achieving the common goals.
     */
    public int getPointsCommonGoal() {
        int point = 0;
        ArrayList<CommonGoalStrategy> commonGoals = match.getCommonGoals();
        for (CommonGoalStrategy c: commonGoals) {
            point += pointCommonGoals(c.getPosition(this));
        }
        return point;
    }

    /**
     *Calculates the points earned by the player for achieving a specific common goal position.
     *The points are determined based on the position.
     *@param position The position achieved by the player in the common goal.
     *@return The points earned for achieving the specified position in the common goal.
     */
    private int pointCommonGoals(int position) {
        if(match.getPlayers().size() == 2){
            return switch (position) {
                case 0 -> 8;
                case 1 -> 4;
                default -> 0;
            };
        }else if (match.getPlayers().size() == 3) {
            return switch (position) {
                case 0 -> 8;
                case 1 -> 6;
                case 2 -> 4;
                default -> 0;
            };
        }else return switch (position) {
                case 0 -> 8;
                case 1 -> 6;
                case 2 -> 4;
                case 3 -> 2;
                default -> 0;
            };
        }

    /**
     *Returns the points earned by the player for achieving the secret goals in their library.
     *The points are calculated based on the blocks in the library matching the secret goals.
     *@return The points earned for achieving the secret goals.
     */
    public int getPointsSecretGoal() {
        Block[][] matrix = Lib.getMatrix();
        int number = 0;
        int[] secret = match.getSecretGoal(this);
        for (int i = 0 ; i < 6 ; i++) {
            if (matrix[secret[i*2]][secret[(i*2)+1]] == Block.values()[i]) number++;
        }
        return switch (number) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 9;
            case 6 -> 12;
            default -> 0;
        };

    }

    /**
     *Checks if the game has ended for the player and takes appropriate actions if so.
     *If the game has ended, sets the player as the last player and signals the end of the game.
     */
    public void checkEndGame() {
        if(match.isEnded()) return;
        for(int i =0 ; i < 5 ; i++) {
            if (Lib.getBlock(0, i) == Block.NULL) return;
        }
        match.setLastPlayer(this);
        match.gameEnded();
    }

    /**
     *Returns the points earned by the player for achieving adjacent blocks in their library.
     *The points are calculated based on the number of adjacent cells in the library
     *using the recursive method calculatePointsAdjacent(int row,int column, int[][] matrix).
     *@return The points earned for achieving adjacent blocks.
     */
    public int getPointsAdjacent() {
        int TotalPoints = 0;
        int numcells;
        int[][] matrix2 = Lib.copyMatrix(Lib.getMatrix());
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                numcells = calculatePointsAdjacent( i , j , matrix2 );
                switch (numcells) {
                    case 0, 1 -> TotalPoints += 0;
                    case 2 -> TotalPoints += 2;
                    case 3 -> TotalPoints += 3;
                    case 4 -> TotalPoints += 5;
                    default -> TotalPoints += 8;
                }
            }

        }
        return TotalPoints;
    }

    /**
     *Calculates the number of adjacent cells of a specific block in the library matrix.
     *The adjacent cells are counted recursively.
     *@param row The row index of the block.
     *@param column The column index of the block.
     *@param matrix The library matrix.
     *@return The number of adjacent cells for the specified block.
     */
    public int calculatePointsAdjacent(int row,int column, int[][] matrix) {
        int color = matrix[row][column];
        int num = 0;
        if (color == -1) return num;
        if (row != 0) {
            if (color == matrix[row - 1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculatePointsAdjacent(row - 1, column, matrix);
            }
        }

        if (column != 4) {
            if (color == matrix[row][column + 1]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculatePointsAdjacent(row, column + 1, matrix);
            }
        }

        if (row != 5) {
            if (color == matrix[row + 1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculatePointsAdjacent(row + 1, column, matrix);
            }
        }

        if (column != 0) {
            if (color == matrix[row][column - 1]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculatePointsAdjacent(row, column - 1, matrix);
            }
        }
        matrix[row][column] = -1;
        return num;
    }

    public String getNickName() {
        return NickName;
    }

    public Library getLib() {
        return Lib;
    }

    public int getPoints() {
        points = getPointsSecretGoal() + getPointsCommonGoal() + getPointsAdjacent() + getPointsFinal();
        return points;
    }

    public int getOtherPoints() {
        points = getPointsCommonGoal() + getPointsAdjacent() + getPointsFinal();
        return points;
    }

    public Match getMatch() {
        return match;
    }
}