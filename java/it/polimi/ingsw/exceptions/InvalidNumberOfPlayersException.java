package it.polimi.ingsw.exceptions;

public class InvalidNumberOfPlayersException extends Exception{
    public InvalidNumberOfPlayersException(){
        super("Number of players must be between 2 and 4");
    }
}
