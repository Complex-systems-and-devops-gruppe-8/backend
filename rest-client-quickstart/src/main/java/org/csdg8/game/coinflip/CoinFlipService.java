package org.csdg8.game.coinflip;

import java.util.Optional;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.csdg8.user.UserService;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class CoinFlipService {

    @Claim(standard = Claims.sub)
    String _userId;

    @Inject
    UserService userService;

    public Long play(CoinFlipState choice, Long betAmount) {
        //TODO pay / subtract money from user

        CoinFlipGame game = new CoinFlipGame(choice, betAmount);

        game.setResult(Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS);
        game.setGameResult(game.getChoice() == game.getResult() ? CoinFlipGameResult.USER_WIN : CoinFlipGameResult.USER_LOSE);
        
        Long gameId = CoinFlipGame.saveGame(game);
        Long userId = Long.valueOf(_userId);
        this.userService.addGameToUser(userId, gameId);

        return gameId;
    }

    public Optional<CoinFlipGame> findById(Long id) {
        return Optional.ofNullable(CoinFlipGame.findById(id));
    }
}
