package it.polimi.ingsw.exceptions;

public class LobbyControllerAlreadyExist extends Exception{
    public LobbyControllerAlreadyExist(){
        super("There is already a lobby controller with that name");
    }
}
