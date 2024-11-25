package org.csdg8.game.coinflip;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.csdg8.game.coinflip.dto.PlayCoinFlipRequest;
import org.csdg8.game.coinflip.model.CoinFlipState;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

@QuarkusTest
public class CoinFlipControllerTest {

    @Inject
    CoinFlipController coinFlipController;

    @Test
    public void shouldThrowConstraintViolationException_whenInvalidBetAmount() {
        PlayCoinFlipRequest request1 = new PlayCoinFlipRequest(CoinFlipState.HEADS, null);
        assertThrowsExactly(ConstraintViolationException.class, () -> {
            coinFlipController.play(request1);
        });

        PlayCoinFlipRequest request2 = new PlayCoinFlipRequest(CoinFlipState.HEADS, 0L);
        assertThrowsExactly(ConstraintViolationException.class, () -> {
            coinFlipController.play(request2);
        });
    }

    @Test
    public void shouldThrowConstraintViolationException_whenInvalidCoinFlipState() {
        PlayCoinFlipRequest request1 = new PlayCoinFlipRequest(null, 10L);
        assertThrowsExactly(ConstraintViolationException.class, () -> {
            coinFlipController.play(request1);
        });
    }
}
