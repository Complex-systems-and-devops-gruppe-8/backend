package org.csdg8.game.coinflip;

import java.util.Optional;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

public class CoinFlipService {

    public Long play(CoinFlipState choice, Long betAmount) {
        //TODO add game to user's list of games
        //TODO pay / subtract money from user

        CoinFlipGame game = new CoinFlipGame();
        game.choice = choice;
        game.betAmount = betAmount;

        game.result = Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS;
        game.gameResult = game.choice == game.result ? CoinFlipGameResult.USER_WIN : CoinFlipGameResult.USER_LOSE;
        
        return CoinFlipGame.saveGame(game);
    }

    public Optional<CoinFlipGame> findById(Long id) {
        return Optional.ofNullable(CoinFlipGame.findById(id));
    }
}
