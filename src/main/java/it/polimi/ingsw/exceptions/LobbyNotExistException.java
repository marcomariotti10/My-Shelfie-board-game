package it.polimi.ingsw.exceptions;

public class LobbyNotExistException extends Exception{
    public LobbyNotExistException(){
        super("There is not lobby with this name");
    }
}
