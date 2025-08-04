package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

import java.util.ArrayList;

public class CommonGoalE extends CommonGoalStrategy{
    private Block[][] libraryMatrix;
    public CommonGoalE() {this.ID = 4;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) {    //three columns of max three different blocks each
        this.libraryMatrix = libraryMatrix;



        for(int i = 0; i < 3; i++){
            if(libraryMatrix[0][i] != Block.NULL && checkColum(i)){
                for(int j = i + 1 ; j < 4; j++){
                    if(libraryMatrix[0][j] != Block.NULL && checkColum(j)){
                        for(int t = j +1; t < 5; t++){
                            if(libraryMatrix[0][t] != Block.NULL && checkColum(t)){
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean checkColum(int i){
        ArrayList<Block> col = new ArrayList<>();
        for(int x = 0; x < 6; x++){
            if(!col.contains(libraryMatrix[x][i])){
                col.add(libraryMatrix[x][i]);
            }
        }
        return col.size() <= 3;
    }
}
