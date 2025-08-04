package it.polimi.ingsw.client.CLI;

import java.io.Serializable;

public enum Colors implements Serializable {
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m");

    private final String color;
    Colors(String color) {
        this.color = color;
    }

    public String getcolor(){
        return color;
    }
}
