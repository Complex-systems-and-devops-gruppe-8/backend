package org.csdg8.game.coinflip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.csdg8.user.User;
import org.csdg8.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class CoinFlipServiceTest {
    @Inject
    CoinFlipService coinFlipService;

    @Inject
    UserService userService;

    private static final Long BET_AMOUNT = 100L;

    private Long userId;

    @BeforeEach
    @Transactional
    void setup() {
        CoinFlipGame.deleteAll();
        User.deleteAll();
        this.userId = User.add("testUser", "password", Set.of("user"));
    }

    @Test
    @Transactional
    void testUserBalanceUpdated() {
        Integer initialBalance = 1000;
        userService.addBalance(userId, initialBalance);

        Long gameId = coinFlipService.playForUser(userId, CoinFlipState.HEADS, BET_AMOUNT);
        
        User updatedUser = User.findById(userId);
        CoinFlipGame game = CoinFlipGame.findById(gameId);
        
        if (game.getGameResult() == CoinFlipGameResult.USER_WIN) {
            assertEquals(initialBalance + BET_AMOUNT.intValue(), updatedUser.getBalance());
        } else {
            assertEquals(initialBalance - BET_AMOUNT.intValue(), updatedUser.getBalance());
        }
    }
}
