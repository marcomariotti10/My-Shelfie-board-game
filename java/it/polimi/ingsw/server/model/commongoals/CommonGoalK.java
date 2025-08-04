package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalK extends CommonGoalStrategy {
    public CommonGoalK() {this.ID = 10;
    }

    @Override
    public boolean check(Block[][] libraryMatrix){   //at least one x of equal blocks
        int i, j;
        for(i = 1; i < 5; i++){
            for(j = 1; j< 4; j++){
                if(libraryMatrix[i][j] != Block.NULL && libraryMatrix[i][j] == libraryMatrix[i+1][j+1] && libraryMatrix[i][j] == libraryMatrix[i+1][j-1] && libraryMatrix[i][j] == libraryMatrix[i-1][j+1] && libraryMatrix[i][j] == libraryMatrix[i-1][j-1]){
                    return true;
                }
            }
        }
        return false;
    }
}
