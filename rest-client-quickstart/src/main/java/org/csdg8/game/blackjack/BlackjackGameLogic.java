package org.csdg8.game.blackjack;

import org.csdg8.game.blackjack.model.BlackjackState;

public class BlackjackGameLogic {
    public BlackjackGame play(BlackjackState choice, Long betAmount) {
        BlackjackGame game = new BlackjackGame(choice, betAmount);
        /* 
        game.setResult(Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS);
        game.setGameResult(game.getChoice() == game.getResult() ? 
            CoinFlipGameResult.USER_WIN : CoinFlipGameResult.USER_LOSE);
          */  
        return game;
    }
}
