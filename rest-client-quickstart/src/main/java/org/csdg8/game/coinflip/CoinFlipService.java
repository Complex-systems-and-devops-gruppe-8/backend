package org.csdg8.game.coinflip;

import java.util.Optional;
import java.util.Set;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.csdg8.user.UserService;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

@RequestScoped
public class CoinFlipService {

    @Claim(standard = Claims.sub)
    String _userId;

    @Inject
    UserService userService;

    public Long play(CoinFlipState choice, Long betAmount) {
        return playForUser(Long.valueOf(_userId), choice, betAmount);
    }

    protected Long playForUser(Long userId, CoinFlipState choice, Long betAmount) {
        CoinFlipGameLogic gameLogic = new CoinFlipGameLogic();
        CoinFlipGame game = gameLogic.play(choice, betAmount);

        Long gameId = persistGameAndUpdateUser(userId, game);

        return gameId;
    }

    private Long persistGameAndUpdateUser(Long userId, CoinFlipGame game) {
        Long gameId = CoinFlipGame.saveGame(game);
        
        this.userService.addGameToUser(userId, gameId);

        Integer betAmount = game.getBetAmount().intValue();
        if (game.getGameResult() == CoinFlipGameResult.USER_WIN) {
            this.userService.addBalance(userId, betAmount);
        } else {
            this.userService.subtractBalance(userId, betAmount);
        }
        
        return gameId;
    }

    public Optional<CoinFlipGame> findById(Long gameId) {
        assert gameId != null;
        return findByIdForUser(Long.valueOf(_userId), gameId);
    }

    public Optional<CoinFlipGame> findByIdForUser(Long userId, Long gameId) {
        assert userId != null;
        assert gameId != null;

        if (!userHasAccessToGame(userId, gameId)) {
            throw new BadRequestException("User " + userId + " does not have access to game with id " + gameId);
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
