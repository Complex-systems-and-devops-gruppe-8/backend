package org.csdg8.game.coinflip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
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
import jakarta.ws.rs.BadRequestException;

@QuarkusTest
class CoinFlipServiceTest {
    @Inject
    CoinFlipService coinFlipService;

    @Inject
    UserService userService;
    private static final Integer INITIAL_BALANCE = 1000;
    private static final Long BET_AMOUNT = 100L;

    private Long userId;

    @BeforeEach
    @Transactional
    void setup() {
        CoinFlipGame.deleteAll();
        User.deleteAll();
        this.userId = User.add("testUser", "password", Set.of("user"));
        userService.addBalance(userId, INITIAL_BALANCE);

    }

    @Test
    @Transactional
    void testUserBalanceUpdated() {
        Long gameId = coinFlipService.playForUser(userId, CoinFlipState.HEADS, BET_AMOUNT);

        User updatedUser = User.findById(userId);
        CoinFlipGame game = CoinFlipGame.findById(gameId);

        if (game.getGameResult() == CoinFlipGameResult.USER_WIN) {
            assertEquals(INITIAL_BALANCE + BET_AMOUNT.intValue(), updatedUser.getBalance());
        } else {
            assertEquals(INITIAL_BALANCE - BET_AMOUNT.intValue(), updatedUser.getBalance());
        }
    }

    @Test
    @Transactional
    void shouldThrowBadRequestException_whenUserIsNotLinkedToGame() {
        Long anotherUserId = User.add("anotherUser", "password", Set.of("user"));

        Integer initialBalance = 1000;
        Long betAmount = 100L;
        this.userService.addBalance(anotherUserId, initialBalance);

        Long gameId = this.coinFlipService.playForUser(anotherUserId, CoinFlipState.HEADS, betAmount);

        assertThrows(BadRequestException.class,
                () -> this.coinFlipService.findByIdForUser(userId, gameId));
    }

    @Test
    @Transactional
    void shouldFindGame_whenUserIsLinkedToGame() {
        Long gameId = this.coinFlipService.playForUser(userId, CoinFlipState.HEADS, BET_AMOUNT);
        Optional<CoinFlipGame> game = this.coinFlipService.findByIdForUser(userId, gameId);

        assertNotEquals(Optional.empty(), game);
        assertEquals(gameId, game.get().id);
    }

    @Test
    @Transactional
    void shouldNotFindGame_whenUserIsNotLinkedToGame() {
        Long gameId = 99999L;

        assertThrows(BadRequestException.class,
                () -> this.coinFlipService.findByIdForUser(userId, gameId));
    }
}
