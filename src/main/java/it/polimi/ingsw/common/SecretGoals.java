package it.polimi.ingsw.common;

import java.util.Arrays;

/**
 * Class containing the patterns of the secret goals
 */

public enum SecretGoals {
    SG_0(0, new int[]{0, 0, 3, 1, 0, 2, 5, 2, 2, 3, 1, 4}),//0
    SG_1(1, new int[]{1, 1, 2, 2, 5, 4, 4, 3, 3, 4, 2, 0}),//1
    SG_2(2, new int[]{2, 2, 1, 3, 1, 0, 3, 4, 5, 0, 3, 1}),//2
    SG_3(3, new int[]{3, 3, 0, 4, 2, 2, 2, 0, 4, 1, 4, 2}),//3
    SG_4(4, new int[]{4, 4, 5, 0, 3, 1, 1, 1, 3, 2, 5, 3}),//4
    SG_5(5, new int[]{5, 0, 4, 1, 4, 3, 0, 2, 2, 3, 0, 4}),//5
    SG_6(6, new int[]{2, 1, 4, 4, 2, 3, 3, 0, 5, 2, 0, 0}),//6
    SG_7(7,new int[]{3, 0, 5, 3, 0, 4, 2, 2, 4, 3, 1, 1}),//7
    SG_8(8, new int[]{4, 4, 0, 2, 5, 0, 4, 1, 3, 4, 2, 2}),//8
    SG_9(9, new int[]{5, 3, 1, 1, 4, 1, 0, 4, 2, 0, 3, 3}),//9
    SG_10(10, new int[]{0, 2, 2, 0, 3, 2, 5, 3, 1, 1, 4, 4}),//10
    SG_11(11, new int[]{1, 1, 4, 4, 2, 2, 3, 3, 0, 2, 5, 0});//11
    private final int index;
    private final int[] coordinates;
    SecretGoals(int index, int[] coordinates){
        this.index = index;
        this.coordinates = coordinates;
    }
    public int getIndex(){
        return index;
    }
    public int[] getCoordinates() {
        return coordinates;
    }
    public static int getIndex(int[] coordinates){
        for (SecretGoals SG: SecretGoals.values() ) {
            if(Arrays.equals(SG.getCoordinates(), coordinates)) return SG.getIndex();
        }
        return -1;
    }
    public static SecretGoals getSG(int index){
        for (SecretGoals s: SecretGoals.values()) {
            if(s.getIndex() == index) return s;
        }
        return null;
    }
}