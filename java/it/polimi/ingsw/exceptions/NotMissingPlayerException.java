package it.polimi.ingsw.exceptions;

public class NotMissingPlayerException extends Exception{
    public NotMissingPlayerException(){
        super("You are not the missing player of this game");
    }
}
