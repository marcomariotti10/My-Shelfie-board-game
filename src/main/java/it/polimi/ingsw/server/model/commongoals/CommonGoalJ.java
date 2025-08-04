package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

import java.util.ArrayList;

public class CommonGoalJ extends CommonGoalStrategy {
    public CommonGoalJ() {this.ID = 9;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) { //two rows of five different blocks each
        ArrayList<Block> row = new ArrayList<>();
        int c = 0;
        for(int i = 0; i < 6 && c < 2; i++){
            for(int j = 0; j < 5; j++){
                if(!row.contains(libraryMatrix[i][j])){
                    row.add(libraryMatrix[i][j]);
                }
            }
            if(!row.contains(Block.NULL) && row.size() > 4) c++;
            row.clear();
        }
        return c == 2;
    }
}
