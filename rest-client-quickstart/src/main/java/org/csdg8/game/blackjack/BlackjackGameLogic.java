package org.csdg8.game.blackjack;

public class BlackjackGameLogic {
    public BlackjackGame play( Long betAmount) {
        BlackjackGame game = new BlackjackGame( betAmount);
         
        return game;
    }

    public BlackjackGame doAction( BlackjackGame game, BlackjackState action) {
        game.doAction( action);
        return game;
    }
}
