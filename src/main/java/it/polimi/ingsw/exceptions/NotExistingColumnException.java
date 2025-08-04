package it.polimi.ingsw.exceptions;

public class NotExistingColumnException extends Exception{
    public NotExistingColumnException(){
        super("The column doesn't exist!\n");
    }
}
