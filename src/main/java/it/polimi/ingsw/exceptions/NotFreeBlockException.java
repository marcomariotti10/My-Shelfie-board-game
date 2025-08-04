package it.polimi.ingsw.exceptions;

public class NotFreeBlockException extends Exception{
    public NotFreeBlockException(){
        super("Block not free");
    }
}
