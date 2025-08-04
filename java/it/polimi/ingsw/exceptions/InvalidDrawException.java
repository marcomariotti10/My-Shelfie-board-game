package it.polimi.ingsw.exceptions;

public class InvalidDrawException extends Throwable {
    public InvalidDrawException(){
        super("you can't draw the choosen Blocks, try again");
    }
}
