package org.csdg8.game.coinflip.dto;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JFieldOption;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

import jakarta.ws.rs.core.MediaType;

@Siren4JEntity(entityClass = "coin-flip", uri = "/game/coin-flip", actions = {
        @Siren4JAction(name = "play", title = "Play coinflip game", method = Method.POST, href = "/game/coin-flip", type = MediaType.APPLICATION_JSON, fields = {
                @Siren4JActionField(name = "choice", title = "Choice", required = true, type = "text", options = {
                        @Siren4JFieldOption(title = "Heads", value = "HEADS"),
                        @Siren4JFieldOption(title = "Tails", value = "TAILS")
                }),
                @Siren4JActionField(name = "betAmount", title = "Bet amount", required = true, type = "number", min = 1)
        }),
        @Siren4JAction(name = "result", title = "Get coinflip game result", method = Method.GET, href = "/game/coin-flip/{id}")
})
public class GetCoinFlipGameResponse {
}