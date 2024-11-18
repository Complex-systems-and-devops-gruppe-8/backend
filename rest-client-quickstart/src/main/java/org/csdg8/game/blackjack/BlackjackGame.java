package org.csdg8.game.blackjack;

import org.csdg8.game.blackjack.model.BlackjackGameResult;
import org.csdg8.game.blackjack.model.BlackjackState;
import org.csdg8.game.blackjack.model.Deck;

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
@Table(name = "blackjack-game")
public class BlackjackGame extends PanacheEntity {
    private BlackjackState choice;
    private BlackjackState result;
    private Deck deck;
    private Long betAmount;
    private BlackjackGameResult gameResult;

    public BlackjackGame(BlackjackState choice, Long betAmount) {
        this.choice = choice;
        this.betAmount = betAmount;
        this.deck = new Deck();
        this.deck.shuffle();
    }

    @Transactional
    public static Long saveGame(BlackjackGame game) {
        assert game.choice != null;
        assert game.result != null;
        assert game.betAmount != null;
        assert game.gameResult != null;
        
        game.persist();

        return game.id;
    }
}
