package org.csdg8.game.coinflip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoinFlipGameLogicTest {
    private CoinFlipGameLogic gameLogic;

    @BeforeEach
    void setup() {
        gameLogic = new CoinFlipGameLogic();
    }

    @Test
    void testGameCreation() {
        CoinFlipState choice = CoinFlipState.HEADS;
        Long betAmount = 100L;

        CoinFlipGame game = gameLogic.play(choice, betAmount);

        assertNotNull(game);
        assertEquals(choice, game.getChoice());
        assertEquals(betAmount, game.getBetAmount());
        assertNotNull(game.getResult());
        assertNotNull(game.getGameResult());
    }

    @Test
    void testWinningGameLogic() {
        CoinFlipState choice = CoinFlipState.HEADS;
        Long betAmount = 100L;

        boolean foundWin = false;
        boolean foundLoss = false;
        
        // may be flakey
        for (int i = 0; i < 100 && (!foundWin || !foundLoss); i++) {
            CoinFlipGame game = gameLogic.play(choice, betAmount);
            
            if (game.getGameResult() == CoinFlipGameResult.USER_WIN) {
                foundWin = true;
                assertEquals(game.getChoice(), game.getResult());
            } else {
                foundLoss = true;
                assertNotEquals(game.getChoice(), game.getResult());
            }
        }
        
        assertTrue(foundWin && foundLoss);
    }
}
