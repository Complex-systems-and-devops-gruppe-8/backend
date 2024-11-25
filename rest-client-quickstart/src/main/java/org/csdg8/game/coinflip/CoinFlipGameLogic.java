package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

public class CoinFlipGameLogic {
    public CoinFlipGame play(CoinFlipState choice, Long betAmount) {
        assert choice != null;
        assert betAmount != null;

        CoinFlipGame game = new CoinFlipGame(choice, betAmount);
        
        game.setResult(Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS);
        game.setGameResult(game.getChoice() == game.getResult() ? 
            CoinFlipGameResult.USER_WIN : CoinFlipGameResult.USER_LOSE);
        
        assert game.getChoice() != null;
        assert game.getBetAmount() != null;
        assert game.getResult() != null;
        assert game.getGameResult() != null;

        return game;
    }
}
