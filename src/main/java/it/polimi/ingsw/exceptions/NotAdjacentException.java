package it.polimi.ingsw.exceptions;

public class NotAdjacentException extends Exception {
    public NotAdjacentException() {
        super("There are not adjacent blocks");
    }
}
