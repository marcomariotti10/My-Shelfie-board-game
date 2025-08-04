package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertSame;

class BlockTest implements Serializable {
    private Block block;
    @BeforeEach
    public void setup(){
        block = Block.BLUE;
    }
    @Test
    void testGetObject(){
        assertSame(block.getObject(), Block.BLUE.getObject());
    }
}