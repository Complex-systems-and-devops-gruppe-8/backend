package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

public class CoinFlipGameLogic {
    public CoinFlipGame play(CoinFlipState choice, Long betAmount) {
        CoinFlipGame game = new CoinFlipGame(choice, betAmount);
        
        game.setResult(Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS);
        game.setGameResult(game.getChoice() == game.getResult() ? 
            CoinFlipGameResult.USER_WIN : CoinFlipGameResult.USER_LOSE);
            
        return game;
    }
}
