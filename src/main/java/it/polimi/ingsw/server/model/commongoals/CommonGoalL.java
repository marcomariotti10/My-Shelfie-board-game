package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalL extends CommonGoalStrategy {
    public CommonGoalL() {this.ID = 11;
    }

    @Override
    public boolean check(Block[][] libraryMatrix){      //stair of block
        int i;
        if(libraryMatrix[5][0] != Block.NULL && libraryMatrix[4][0] == Block.NULL){
            for(i = 1; i < 5; i++){
                if(!(libraryMatrix[5-i][i] != Block.NULL && libraryMatrix[4-i][i] == Block.NULL)){
                    return false;
                }
            }
            return true;
        }
        else if(libraryMatrix[4][0] != Block.NULL && libraryMatrix[3][0] == Block.NULL){
            for(i = 0; i < 5; i++){
                if(!(libraryMatrix[4-i][i] != Block.NULL && (i ==4 || libraryMatrix[3-i][i] == Block.NULL))){
                    return false;
                }
            }
            return true;
        }
        else if(libraryMatrix[5][4] != Block.NULL && libraryMatrix[4][4] == Block.NULL){
            for(i = 1; i < 5; i++){
                if(!(libraryMatrix[5-i][4-i] != Block.NULL && libraryMatrix[4-i][4-i] == Block.NULL)){
                    return false;
                }
            }
            return true;
        }
        else if(libraryMatrix[4][4] != Block.NULL && libraryMatrix[3][4] == Block.NULL){
            for(i = 0; i < 5; i++){
                if(!(libraryMatrix[4-i][4-i] != Block.NULL && (i ==4 || libraryMatrix[3-i][4-i] == Block.NULL))){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
}
