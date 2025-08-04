package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

import java.util.ArrayList;

public class CommonGoalI extends CommonGoalStrategy {
    public CommonGoalI() {this.ID = 8;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) { //two columns of six different blocks each
        ArrayList<Block> col = new ArrayList<>();
        int c = 0;
        for(int j = 0; j < 5 && c < 2; j++){
            for(int i = 0; i < 6; i++){
                if(!col.contains(libraryMatrix[i][j])){
                    col.add(libraryMatrix[i][j]);
                }
            }
            if(!col.contains(Block.NULL) && col.size() > 5) c++;
            col.clear();
        }
        return c == 2;
    }
}