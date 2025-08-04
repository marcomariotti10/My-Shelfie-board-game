package it.polimi.ingsw.exceptions;

public class IllegalNumberOfPlayers extends Exception{
    public IllegalNumberOfPlayers(){
        super("Number of players should be between 2 and 4");
    }
}
