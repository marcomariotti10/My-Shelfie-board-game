package it.polimi.ingsw.exceptions;

public class ExceedNumberOfPlayersException extends Exception{
    public ExceedNumberOfPlayersException(){
        super("Number of players already reached");
    }
}
