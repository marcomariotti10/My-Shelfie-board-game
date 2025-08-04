package it.polimi.ingsw.exceptions;

public class ExceedNumberOfAvailableBlocksException extends Exception{
    public ExceedNumberOfAvailableBlocksException(){
        super("Number of blocks to draw exceed the number of the avaiable blocks");
    }
}
