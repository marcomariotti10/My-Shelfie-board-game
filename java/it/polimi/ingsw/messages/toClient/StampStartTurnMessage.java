package it.polimi.ingsw.messages.toClient;

import it.polimi.ingsw.server.model.Block;

import java.util.ArrayList;

public class StampStartTurnMessage  implements ClientMessage{
    private final Block[][] playerlibrary;
    private final int playerpoints;
    private final Block[][] gameboard;
    private final int[] secret;
    private final ArrayList<Block[][]> otherplayerslibrary;
    private final int[] otherplayerspoints;
    private final String[] otherplayersNickName;
    private final int [] commongoals;
    private final int [] achivedNum;
    private final int [] achivedCG;
    private final String name;
    private final boolean boardUpgrade;
    public StampStartTurnMessage(String name,Block[][] playerlibrary, int playerpoints, Block[][] gameboard, int[] secret,int[] achivedCG, ArrayList<Block[][]> otherplayerslibrary, int[] otherplayerspoints, String[] otherplayersNickName, int[] commongoals, int[] achivedNum, Boolean boardUpgrade) {        this.commongoals = commongoals;
        this.playerpoints = playerpoints;
        this.gameboard = gameboard;
        this.otherplayerslibrary = otherplayerslibrary;
        this.otherplayersNickName = otherplayersNickName;
        this.playerlibrary = playerlibrary;
        this.otherplayerspoints = otherplayerspoints;
        this.secret = secret;
        this.achivedNum = achivedNum;
        this.achivedCG = achivedCG;
        this.name = name;
        this.boardUpgrade = boardUpgrade;
    }
    public ArrayList<Block[][]> getOtherplayerslibrary() {
        return otherplayerslibrary;
    }
    public Block[][] getGameboard() {
        return gameboard;
    }
    public Block[][] getPlayerlibrary() {
        return playerlibrary;
    }
    public int getPlayerPoints() {
        return playerpoints;
    }
    public int[] getCommongoals() {
        return commongoals;
    }
    public int[] getOtherplayerspoints() {
        return otherplayerspoints;
    }
    public int[] getSecret() {
        return secret;
    }
    public String[] getOtherplayersNickName() {
        return otherplayersNickName;
    }
    public int[] getAchivedNum() {
        return achivedNum;
    }
    public int[] getAchivedCG() {
        return achivedCG;
    }
    public String getName() {
        return name;
    }
    public boolean isBoardUpgrade() {
        return boardUpgrade;
    }
}