package it.polimi.ingsw.server.model;

import java.io.Serializable;

/**
 *Enumeration for the types of possible blocks.
 *@author Andrea Gollo, Gabriele Marra, Marco Mariotti
 */
public enum Block implements Serializable {
    PINK(0, "Piante"),
    ORANGE(1, "Giochi"),
    BLUE(2, "Cornici"),
    LIGHT_BLUE(3, "Trofei"),
    WHITE(4, "Libri"),
    GREEN(5, "Gatti"),
    NULL(-1, "vuoto");

    private final int index;
    private final String object;

    Block(int index, String object){
        this.index = index;
        this.object = object;
    }

    /**
     * @return the index of the selected block
     */
    public int getIndex(){
        return index;
    }

    /**
     * @return the string associated to the block
     */
    public String getObject(){
        return object;
    }
}