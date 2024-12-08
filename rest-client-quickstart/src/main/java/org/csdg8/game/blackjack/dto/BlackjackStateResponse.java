package org.csdg8.game.blackjack.dto;

 
import org.csdg8.game.blackjack.model.BlackjackGameResult;
import org.csdg8.game.blackjack.model.CardHand;

import com.google.code.siren4j.annotations.Siren4JEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Siren4JEntity(entityClass = "blackjack-game", uri = "/game/blackjack/{id}")
public class BlackjackStateResponse {
    private CardHand dealerHand;
    private CardHand playerHand;
    private Long betAmount;
    private long id;
    private BlackjackGameResult gameResult;

}