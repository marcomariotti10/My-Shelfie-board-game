package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Block;
import it.polimi.ingsw.server.model.CommonGoalStrategy;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalStrategyTest {

    private CommonGoalStrategy commonGoalStrategy;

    @BeforeEach
    void setUp() {
        commonGoalStrategy = new CommonGoalStrategy() {
            @Override
            public boolean check(Block[][] libraryMatrix) {
                // Custom implementation for testing purposes
                boolean allBlueBlocks = true;
                for(Block[] row : libraryMatrix){
                    for(Block block : row){
                        if(allBlueBlocks){
                            if(!block.equals(Block.BLUE)){
                                allBlueBlocks = false;
                            }
                        }
                    }
                }
                return allBlueBlocks;
            }
        };
    }

    @Test
    void check_ShouldReturnTrue() {
        Block[][] libraryMatrix = {
                {Block.BLUE, Block.BLUE},
                {Block.BLUE, Block.BLUE}
        };

        boolean result = commonGoalStrategy.check(libraryMatrix);

        assertTrue(result);
    }

    @Test
    void getPosition_PlayerInRanking_ShouldReturnPosition() {
        Player player1 = new Player("Player 1", null);
        Player player2 = new Player("Player 2", null);
        Player player3 = new Player("Player 3", null);

        commonGoalStrategy.addPlayer(player1);
        commonGoalStrategy.addPlayer(player2);
        commonGoalStrategy.addPlayer(player3);

        int position = commonGoalStrategy.getPosition(player2);

        assertEquals(1, position);
    }

    @Test
    void getPosition_PlayerNotInRanking_ShouldReturnNegativeOne() {
        Player player1 = new Player("Player 1", null);
        Player player2 = new Player("Player 2", null);

        commonGoalStrategy.addPlayer(player1);

        int position = commonGoalStrategy.getPosition(player2);

        assertEquals(-1, position);
    }

    @Test
    void addPlayer_ShouldAddPlayerToRanking() {
        Player player = new Player("Player 1", null);

        commonGoalStrategy.addPlayer(player);

        assertTrue(commonGoalStrategy.rankingPlayer.contains(player.getNickName()));
    }

    @Test
    void getID_ShouldReturnID() {
        int expectedID = 10;
        commonGoalStrategy.ID = expectedID;

        int result = commonGoalStrategy.getID();

        assertEquals(expectedID, result);
    }
}
