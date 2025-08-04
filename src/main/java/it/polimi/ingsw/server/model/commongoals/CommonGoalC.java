package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalC extends   CommonGoalStrategy {
    public CommonGoalC() {this.ID = 2;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) {   //four same blocks in the corners
        if(libraryMatrix[0][0] != Block.NULL && libraryMatrix[0][4] != Block.NULL && libraryMatrix[5][0] != Block.NULL && libraryMatrix[5][4] != Block.NULL){
            return libraryMatrix[0][0] == libraryMatrix[0][4] && libraryMatrix[0][0] == libraryMatrix[5][0] && libraryMatrix[0][0] == libraryMatrix[5][4];
        }
        return false;

    }
}