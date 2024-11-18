package org.csdg8.game.blackjack;


import java.util.Optional;

import org.csdg8.game.blackjack.model.BlackjackGameResult;
import org.csdg8.game.blackjack.model.BlackjackState;
import org.csdg8.user.UserService;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlackjackService {

    @Claim(standard = Claims.sub)
    String _userId;

    @Inject
    UserService userService;

    public Long play(BlackjackState choice, Long betAmount) {
        return playForUser(Long.valueOf(_userId), choice, betAmount);
    }

    protected Long playForUser(Long userId, BlackjackState choice, Long betAmount) {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        BlackjackGame game = gameLogic.play(choice, betAmount);

        Long gameId = persistGameAndUpdateUser(userId, game);

        return gameId;
    }

    private Long persistGameAndUpdateUser(Long userId, BlackjackGame game) {
        Long gameId = BlackjackGame.saveGame(game);
        
        this.userService.addGameToUser(userId, gameId);

        Integer betAmount = game.getBetAmount().intValue();
        if (game.getGameResult() == BlackjackGameResult.USER_WIN) {
            this.userService.addBalance(userId, betAmount);
        } else {
            this.userService.subtractBalance(userId, betAmount);
        }
        
        return gameId;
    }

    public Optional<BlackjackGame> findById(Long id) {
        return Optional.ofNullable(BlackjackGame.findById(id));
    }
}
