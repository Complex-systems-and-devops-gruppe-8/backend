package org.csdg8.game.dto;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JLink;

@Siren4JEntity(entityClass = "game", uri = "/game", links = {
        @Siren4JLink(rel = "coin-flip", href = "/game/coin-flip"),
        @Siren4JLink(rel = "blackjack", href = "/game/blackjack")
})
public class GetGameResponse {
}
