package org.csdg8.game.blackjack.dto;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JFieldOption;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

import jakarta.ws.rs.core.MediaType;

@Siren4JEntity(entityClass = "blackjack", uri = "/game/blackjack", actions = {
        @Siren4JAction(name = "start-game", title = "Create a bj game", method = Method.POST, href = "/game/blackjack", type = MediaType.APPLICATION_JSON, fields = {
                @Siren4JActionField(name = "betAmount", title = "Bet amount", required = true, type = "number", min = 1)
        }),
        @Siren4JAction(name = "do-action", title = "Hit or stand in bj game", method = Method.POST, href = "/game/blackjack/{id}", type = MediaType.APPLICATION_JSON, fields = 
        {
                        @Siren4JActionField(name = "choice", title = "Choice", required = true, type = "text", options = {
                                   @Siren4JFieldOption(title = "Hit", value = "HIT"),
                                @Siren4JFieldOption(title = "Stand", value = "STAND")
                        
                })
        })

})
public class GetBlackjackGameResponse {
}