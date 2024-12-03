package org.csdg8.game.blackjack;
import java.time.LocalDateTime;

import org.csdg8.game.blackjack.model.BlackjackGameResult;
import org.csdg8.game.blackjack.model.BlackjackState;
import org.csdg8.game.blackjack.model.CardHand;
import org.csdg8.game.blackjack.model.CardHandConverter;
import org.csdg8.game.blackjack.model.Deck;
import org.csdg8.game.blackjack.model.DeckConverter;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Convert(converter = DeckConverter.class)
    @Column(length = 1000)
    private Deck deck;
    
    @Convert(converter = CardHandConverter.class)
    private CardHand playerHand;
    @Convert(converter = CardHandConverter.class)
    private CardHand dealerHand;
    private Long betAmount;
    private BlackjackGameResult gameResult;
    private LocalDateTime createdAt;

    public BlackjackGame(  Long betAmount, BlackjackGameResult gameResult) {
        this.betAmount = betAmount;
        this.deck = new Deck();
        this.playerHand = new CardHand();
        this.dealerHand = new CardHand();
        this.createdAt = LocalDateTime.now();
        this.gameResult = gameResult;
        this.deck.shuffle();
    }
    public void setGameResult(BlackjackGameResult gameResult) {
        this.gameResult = gameResult;
    }

    @Transactional
    public static Long saveGame(BlackjackGame game) {
        
        if (game.id == null) {
            game.persist(); // New entity
        } else {
            getEntityManager().merge(game); // Update existing entity
        }
        return game.id;
    }
    @Transactional
    public static Long updateGameState(BlackjackGame game) {
        assert game.choice != null;
        assert game.result != null;
        assert game.betAmount != null;
        assert game.gameResult != null;
        
        game.persist();

        return game.id;
    }

}
