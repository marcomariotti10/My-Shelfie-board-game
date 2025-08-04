package it.polimi.ingsw.messages.toServer;

import java.util.*;

public class DrawBlocksMessage implements ServerMessage {
    private Set<ArrayList<Integer>> positions;

    public DrawBlocksMessage(Set<ArrayList<Integer>> positions){
        this.positions = positions;
        System.out.println("queste sono le coordinate salvate nel mex" + positions);

    }

    public Set<ArrayList<Integer>> getPositions(){
        System.out.println("queste sono le coordinate nel mex" + positions);

        return positions;
    }
}