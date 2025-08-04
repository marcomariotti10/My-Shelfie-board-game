package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalD extends CommonGoalStrategy {
    public CommonGoalD() {this.ID = 3;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) { //two groups of four blocks in square shape
        //check color by color
        for(int i = 0; i<Block.values().length-1;i++){ //length - 1 to exclude Block.NULL
            Block color = Block.values()[i];
            for (Block[] matrix : libraryMatrix) {
                System.arraycopy(matrix, 0, matrix, 0, libraryMatrix[0].length);
            }
            int count = 0; //to count how many square of the same color
            for(int r = 0; r< libraryMatrix.length - 1; r++){
                for(int c = 0; c< libraryMatrix[0].length; c++){
                    if(libraryMatrix[r][c] == color && libraryMatrix[r+1][c] == color && libraryMatrix[r][c+1] == color && libraryMatrix[r+1][c+1] == color){
                        if(checkIsolate(libraryMatrix,r,c)){
                            //substitute with Block.NULL to not count twice the same
                            libraryMatrix[r][c] = Block.NULL;
                            libraryMatrix[r+1][c] = Block.NULL;
                            libraryMatrix[r][c+1] = Block.NULL;
                            libraryMatrix[r+1][c+1] = Block.NULL;
                            count++;
                            if(count>=2){ // two isolate squares are found
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    private boolean checkIsolate(Block[][] libraryMatrix, int r, int c){
        Block color = libraryMatrix[r][c];

        if(r > 0) {
            if (libraryMatrix[r - 1][c].equals(color) || libraryMatrix[r - 1][c + 1].equals(color)) return false;
        }
        if(r < libraryMatrix.length - 2)
            if(libraryMatrix[r+2][c].equals(color) || libraryMatrix[r+2][c+1].equals(color)) return false;
        if(c > 0){
            if (libraryMatrix[r][c-1].equals(color) || libraryMatrix[r + 1][c - 1].equals(color)) return false;
        }
        if(c < libraryMatrix[0].length - 2)
            return !libraryMatrix[r][c + 2].equals(color) && !libraryMatrix[r + 1][c + 2].equals(color);

        return true;
    }
}