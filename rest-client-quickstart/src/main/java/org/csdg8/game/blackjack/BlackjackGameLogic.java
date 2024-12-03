package org.csdg8.game.blackjack;

import org.csdg8.game.blackjack.model.BlackjackGameResult;

public class BlackjackGameLogic {
    public BlackjackGame creatGame( Long betAmount) {
        BlackjackGame game = new BlackjackGame( betAmount, BlackjackGameResult.ONGOING);
        game = dealStartCards( game);
        
        return game;
    }
    
    private BlackjackGame dealStartCards( BlackjackGame game) {
        game.getPlayerHand().addCard( game.getDeck().drawCard());
        game.getPlayerHand().addCard( game.getDeck().drawCard());
        game.getDealerHand().addCard( game.getDeck().drawCard());
        game.getDealerHand().addCard(game.getDeck().getHiddenCard());
        return game;
    }

    public BlackjackGame hit( BlackjackGame game) {
        if (game.getPlayerHand().calculateHandValue() > 21) {
            return game;
        }
        game.getPlayerHand().addCard( game.getDeck().drawCard());
        if (game.getPlayerHand().calculateHandValue() > 21) {
            
            game = stand( game);
            
        }
        return game;
    }
    public BlackjackGame stand( BlackjackGame game) {
        while (game.getDealerHand().calculateHandValue() < 17) {
            game.getDealerHand().removeBlankCard();
            game.getDealerHand().addCard( game.getDeck().drawCard());
        }
        //player bust
        if (game.getPlayerHand().calculateHandValue() > 21) {
            game.setGameResult( BlackjackGameResult.DEALER_WIN);
        } else if (game.getPlayerHand().calculateHandValue()>game.getDealerHand().calculateHandValue()) {
            game.setGameResult( BlackjackGameResult.USER_WIN);
        } else if (game.getDealerHand().calculateHandValue() > 21 && game.getPlayerHand().calculateHandValue() <= 21) {
            game.setGameResult( BlackjackGameResult.USER_WIN);
        } else if (game.getDealerHand().calculateHandValue() == game.getPlayerHand().calculateHandValue()) {
            game.setGameResult( BlackjackGameResult.DRAW);
        }
        return game;
    }
}
