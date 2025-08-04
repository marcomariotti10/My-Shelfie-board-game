package it.polimi.ingsw.exceptions;

public class NullBlockException extends Exception{
    public NullBlockException(){
        super("Null block not allowed");
    }
}
