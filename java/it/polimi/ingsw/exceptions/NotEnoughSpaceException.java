package it.polimi.ingsw.exceptions;

public class NotEnoughSpaceException extends Exception{
    public NotEnoughSpaceException(){
        super("Not enough space in the library!");
    }
}
