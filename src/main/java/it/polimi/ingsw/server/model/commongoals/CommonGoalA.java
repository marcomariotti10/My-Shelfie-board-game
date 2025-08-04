package it.polimi.ingsw.server.model.commongoals;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;

public class CommonGoalA extends CommonGoalStrategy {
    public CommonGoalA() {this.ID = 0;
    }
    @Override
    public boolean check(Block[][] libraryMatrix) { // six separate groups

        int[][] copy = new int[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                copy[i][j] = libraryMatrix[i][j].getIndex();
            }
        }

        int Total = 0;
        int numcells;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                numcells = 0;
                numcells = calculateNumber(i, j, copy);
                if (numcells == 1) {
                    Total += 1; //il numcell conta una cella in meno rispetto al totale
                } else {
                    Total += 0;
                }
            }
        }
        return Total >= 6;
    }

    public int calculateNumber(int row, int column, int[][] matrix) {
        int color = matrix[row][column];
        int num = 0;
        if (color == -1) return num;

        if (row != 0) {
            if (color == matrix[row - 1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row - 1, column, matrix);
            }
        }

        if (column != 4) {
            if (color == matrix[row][column + 1]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row, column + 1, matrix);
            }
        }

        if (row != 5) {
            if (color == matrix[row + 1][column]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row + 1, column, matrix);
            }
        }

        if (column != 0) {
            if (color == matrix[row][column - 1]) {
                num = num + 1;
                matrix[row][column] = -1;
                num = num + calculateNumber(row, column - 1, matrix);
            }
        }
        return num;
    }

}




