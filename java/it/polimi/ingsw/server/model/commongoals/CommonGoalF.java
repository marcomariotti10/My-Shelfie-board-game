package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalF extends CommonGoalStrategy {
    public CommonGoalF() {this.ID = 5;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) { //eight blocks of the same tipe
        int count = 0;
        Block current = null;


        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.PINK){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        count = 0;
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.ORANGE){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        count = 0;
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.BLUE){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        count = 0;
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.LIGHT_BLUE){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        count = 0;
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.WHITE){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        count = 0;
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 5; j++){
                if(libraryMatrix[i][j] == Block.GREEN){
                    count ++;
                }
            }
        }
        if(count >= 8){
            return true;
        }
        return false;
    }
}
