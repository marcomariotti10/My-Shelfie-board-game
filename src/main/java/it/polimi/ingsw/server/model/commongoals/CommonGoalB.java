package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalB extends   CommonGoalStrategy {
    public CommonGoalB() {this.ID = 1;
    }

    @Override
    public boolean check(Block[][] libraryMatrix) {  //four columns with four same blocks

        int[][] copy = new int[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                copy[i][j] = libraryMatrix[i][j].getIndex();
            }
        }

        int Total = 0;
        int numcells = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                numcells = 0;
                numcells = calculateNumber(i, j, copy);
                if (numcells == 3) {
                    Total += 1; //il numcell conta una cella in meno rispetto al totale
                } else {
                    Total += 0;
                }
            }
        }
        return Total >= 4;
    }

    public int calculateNumber(int row,int column, int[][] matrix) {
        int color = matrix[row][column];
        int num = 0;
        if (color == -1) return num;

        if(row != 0) {
            if(color == matrix[row-1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row - 1, column, matrix);
            }
        }

        if(column != 4 ) {
            if(color == matrix[row][column+1]) {
                num = num +1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row, column+1, matrix);
            }
        }

        if(row != 5) {
            if(color == matrix[row+1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row + 1, column, matrix);
            }
        }

        if(column != 0 ) {
            if(color == matrix[row][column-1]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row, column-1, matrix);
            }
        }
        return num;
    }
}