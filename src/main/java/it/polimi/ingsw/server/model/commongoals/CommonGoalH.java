package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

import java.util.ArrayList;

public class CommonGoalH extends CommonGoalStrategy {
    Block[][] libraryMatrix;
    public CommonGoalH() {this.ID = 7;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) {     //four rows of max three different blocks each
        this.libraryMatrix = libraryMatrix;
        for(int i = 0; i < 3; i++){
            if(libraryMatrix[i][0] != Block.NULL && libraryMatrix[i][1] != Block.NULL &&
                    libraryMatrix[i][2] != Block.NULL && libraryMatrix[i][3] != Block.NULL &&
                    libraryMatrix[i][4] != Block.NULL && checkRow(i)){
                for(int j = i + 1; j < 4; j++){
                    if(libraryMatrix[j][0] != Block.NULL && libraryMatrix[j][1] != Block.NULL &&
                            libraryMatrix[j][2] != Block.NULL && libraryMatrix[j][3] != Block.NULL &&
                            libraryMatrix[j][4] != Block.NULL && checkRow(j)){
                        for(int t = j + 1; t < 5; t ++){
                            if(libraryMatrix[t][0] != Block.NULL && libraryMatrix[t][1] != Block.NULL &&
                                    libraryMatrix[t][2] != Block.NULL && libraryMatrix[t][3] != Block.NULL &&
                                    libraryMatrix[t][4] != Block.NULL && checkRow(t)){
                                for(int z = t +1; t < 6; t++){
                                    if(libraryMatrix[z][0] != Block.NULL && libraryMatrix[z][1] != Block.NULL &&
                                            libraryMatrix[z][2] != Block.NULL && libraryMatrix[z][3] != Block.NULL &&
                                            libraryMatrix[z][4] != Block.NULL && checkRow(z)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    public boolean checkRow(int i){
        ArrayList<Block> row = new ArrayList<>();
        for(int x = 0; x < 5; x++){
            if(!row.contains(libraryMatrix[i][x])){
                row.add(libraryMatrix[i][x]);
            }
        }
        return row.size() <= 3;
    }
}
