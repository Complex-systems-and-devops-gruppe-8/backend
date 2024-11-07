package org.csdg8.game.coinflip.dto;

import org.csdg8.game.coinflip.model.CoinFlipGameResult;
import org.csdg8.game.coinflip.model.CoinFlipState;

import com.google.code.siren4j.annotations.Siren4JEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Siren4JEntity(entityClass = "coin-flip-game", uri = "/game/coin-flip/{id}")
public class GetCoinFlipGameResponse {
    private Long id;
    private CoinFlipState userChoice;
    private CoinFlipState result;
    private Long betAmount;
    private CoinFlipGameResult gameResult;
}