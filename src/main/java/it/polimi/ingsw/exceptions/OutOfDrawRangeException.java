package it.polimi.ingsw.exceptions;

public class OutOfDrawRangeException extends Exception{
    public OutOfDrawRangeException(){
        super("You must draw from 1 to 3 blocks");
    }
}
