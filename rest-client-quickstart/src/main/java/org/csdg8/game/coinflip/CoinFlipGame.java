package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "coin-flip-game")
public class CoinFlipGame extends PanacheEntity {
    public CoinFlipState choice;
    public CoinFlipState result;
    public Long betAmount;
    public CoinFlipGameResult gameResult;

    @Transactional
    public static Long play(CoinFlipState choice, Long betAmount) {
        assert choice != null;
        assert betAmount > 0;

        CoinFlipGame game = new CoinFlipGame();
        game.choice = choice;
        game.betAmount = betAmount;
        game.result = Math.random() < 0.5 ? CoinFlipState.HEADS : CoinFlipState.TAILS;

        if (game.choice == game.result) {
            game.gameResult = CoinFlipGameResult.USER_WIN;
        } else {
            game.gameResult = CoinFlipGameResult.USER_LOSE;
        }

        assert game.choice != null;
        assert game.result != null;
        assert game.betAmount != null;
        assert game.gameResult != null;

        game.persist();
        return game.id;
    }
}
