package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalG extends CommonGoalStrategy {
    public CommonGoalG() {this.ID = 6;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) {     //diagonal of length 5 with equals blocks
        if(libraryMatrix[5][0] != Block.NULL && libraryMatrix[5][0] == libraryMatrix[4][1] && libraryMatrix[5][0] == libraryMatrix[3][2] && libraryMatrix[5][0] == libraryMatrix[2][3] && libraryMatrix[5][0] == libraryMatrix[1][4]){
            return true;
        }
        if(libraryMatrix[4][0] != Block.NULL && libraryMatrix[4][0] == libraryMatrix[3][1] && libraryMatrix[4][0] == libraryMatrix[2][2] && libraryMatrix[4][0] == libraryMatrix[1][3] && libraryMatrix[4][0] == libraryMatrix[0][4]){
            return true;
        }
        if(libraryMatrix[5][4] != Block.NULL && libraryMatrix[5][4] == libraryMatrix[4][3] && libraryMatrix[5][4] == libraryMatrix[3][2] && libraryMatrix[5][4] == libraryMatrix[2][1] && libraryMatrix[5][4] == libraryMatrix[1][0]){
            return true;
        }
        return libraryMatrix[4][4] != Block.NULL && libraryMatrix[4][4] == libraryMatrix[3][3] && libraryMatrix[4][4] == libraryMatrix[2][2] && libraryMatrix[4][4] == libraryMatrix[1][1] && libraryMatrix[4][4] == libraryMatrix[0][0];
    }
}
