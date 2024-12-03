package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "coin-flip-game")
public class CoinFlipGame extends PanacheEntity {
    private CoinFlipState choice;
    private CoinFlipState result;
    private Long betAmount;
    private CoinFlipGameResult gameResult;

    public CoinFlipGame(CoinFlipState choice, Long betAmount) {
        assert choice != null;
        assert betAmount != null;

        this.choice = choice;
        this.betAmount = betAmount;
    }

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
