package it.polimi.ingsw.exceptions;

public class ExceededLibraryLimitException extends Exception{
    public ExceededLibraryLimitException(){
        super("Exceeded column lenght!\n");
    }
}
