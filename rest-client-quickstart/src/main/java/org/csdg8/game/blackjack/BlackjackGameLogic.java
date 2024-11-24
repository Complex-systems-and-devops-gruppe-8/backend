package org.csdg8.game.blackjack;

public class BlackjackGameLogic {
    public BlackjackGame creatGame( Long betAmount) {
        BlackjackGame game = new BlackjackGame( betAmount);
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
        return game;
    }
}
