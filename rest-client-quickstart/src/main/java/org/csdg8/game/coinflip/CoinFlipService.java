package org.csdg8.game.coinflip;

import java.util.Optional;
import java.util.Set;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.csdg8.model.exception.UserNotLinkedToGameException;
import org.csdg8.user.UserService;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class CoinFlipService {

    @Claim(standard = Claims.sub)
    String _userId;

    @Inject
    UserService userService;

    public Long play(CoinFlipState choice, Long betAmount) {
        assert choice != null;
        assert betAmount != null;
        assert betAmount > 0;

        return playForUser(Long.valueOf(_userId), choice, betAmount);
    }

    protected Long playForUser(Long userId, CoinFlipState choice, Long betAmount) {
        assert userId != null;
        assert choice != null;
        assert betAmount != null;
        assert betAmount > 0;

        CoinFlipGameLogic gameLogic = new CoinFlipGameLogic();
        CoinFlipGame game = gameLogic.play(choice, betAmount);

        Long gameId = persistGameAndUpdateUser(userId, game);

        assert gameId != null;

        return gameId;
    }

    private Long persistGameAndUpdateUser(Long userId, CoinFlipGame game) {
        assert userId != null;

        assert game != null;
        assert game.getChoice() != null;
        assert game.getResult() != null;
        assert game.getBetAmount() != null;
        assert game.getGameResult() != null;

        Long gameId = CoinFlipGame.saveGame(game);
        
        this.userService.addGameToUser(userId, gameId);

        Integer betAmount = game.getBetAmount().intValue();
        if (game.getGameResult() == CoinFlipGameResult.USER_WIN) {
            this.userService.addBalance(userId, betAmount);
        } else {
            this.userService.removeBalance(userId, betAmount);
        }
        
        return gameId;
    }

    public Optional<CoinFlipGame> findById(Long gameId) {
        assert gameId != null;
        return findByIdForUser(Long.valueOf(_userId), gameId);
    }

    @Transactional
    public Optional<CoinFlipGame> findByIdForUser(Long userId, Long gameId) {
        assert userId != null;
        assert gameId != null;

        if (!userHasAccessToGame(userId, gameId)) {
            throw new UserNotLinkedToGameException("User " + userId + " does not have access to coin-flip game with id " + gameId);
        }

        return Optional.ofNullable(CoinFlipGame.findById(gameId));
    }

    private boolean userHasAccessToGame(Long userId, Long gameId) {
        assert userId != null;
        assert gameId != null;

        Set<Long> userGames = this.userService.getUserGames(userId);
        return userGames != null && userGames.contains(gameId);
    }
}
