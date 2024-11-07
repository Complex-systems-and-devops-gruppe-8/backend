package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
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
    public static Long saveGame(CoinFlipGame game) {
        assert game.choice != null;
        assert game.result != null;
        assert game.betAmount != null;
        assert game.gameResult != null;
        
        game.persist();

        return game.id;
    }
}
