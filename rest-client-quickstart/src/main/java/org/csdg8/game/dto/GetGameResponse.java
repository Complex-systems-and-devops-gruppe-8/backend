package org.csdg8.game.dto;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JLink;

@Siren4JEntity(entityClass = "game", uri = "/game", links = {
        @Siren4JLink(rel = "coin-flip-game", href = "/game/coin-flip"),
})
public class GetGameResponse {
}
