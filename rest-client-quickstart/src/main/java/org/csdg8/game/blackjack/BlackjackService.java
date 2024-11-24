package org.csdg8.game.blackjack;


import java.util.Optional;

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

    public BlackjackGame createGame(Long betAmount) {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        BlackjackGame game = gameLogic.creatGame(betAmount);
     
        persistGameAndUpdateUser(Long.valueOf(_userId), game);
        return game;
    }

    public BlackjackGame hit(BlackjackGame game) {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        game = gameLogic.hit(game);
       
        //check if this is needed
        //Long gameId = BlackjackGame.saveGame(game);

        return game;
    }

    public BlackjackGame stand(BlackjackGame game) {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        game = gameLogic.stand(game );
       //check if this is needed
       // BlackjackGame.saveGame(game);
       return game;
    }

    /* 
    protected Long playForUser(Long userId, Long betAmount) {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        BlackjackGame game = gameLogic.play(betAmount);

        Long gameId = persistGameAndUpdateUser(userId, game);

        return gameId;
    }*/

    private Long persistGameAndUpdateUser(Long userId, BlackjackGame game) {

        Long gameId = BlackjackGame.saveGame(game);
        
        this.userService.addGameToUser(userId, gameId);
        /* should maybe be added later
        Integer betAmount = game.getBetAmount().intValue();
        if (game.getGameResult() == BlackjackGameResult.USER_WIN) {
            this.userService.addBalance(userId, betAmount);
        } else {
            this.userService.subtractBalance(userId, betAmount);
        }*/
        
        return gameId;
    }

    public Optional<BlackjackGame> findById(Long id) {
        return Optional.ofNullable(BlackjackGame.findById(id));
    }

    public void updateGame(BlackjackGame game) {
        BlackjackGame.saveGame(game);
    }
}
