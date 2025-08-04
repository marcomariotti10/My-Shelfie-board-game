package it.polimi.ingsw.exceptions;

public class OutOfTakeRangeException extends Exception{
    public OutOfTakeRangeException(){
        super("You must take from 1 to 3 blocks");
    }
}
