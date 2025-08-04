package it.polimi.ingsw.exceptions;

public class AlreadyExistNickException extends Exception{
    public AlreadyExistNickException(){
        super("This nick is already present in the lobby");
    }
}