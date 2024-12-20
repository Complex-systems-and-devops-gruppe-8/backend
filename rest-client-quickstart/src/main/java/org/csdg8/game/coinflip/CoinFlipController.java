package org.csdg8.game.coinflip;

import java.net.URI;

import org.csdg8.game.coinflip.dto.GetCoinFlipGameResponse;
import org.csdg8.game.coinflip.dto.GetCoinFlipGameResultResponse;
import org.csdg8.game.coinflip.dto.PlayCoinFlipRequest;
import org.csdg8.model.exception.GameNotFoundException;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CoinFlipController {

    @Inject
    CoinFlipService coinFlipService;

    public Response play(@Valid @NotNull PlayCoinFlipRequest request) {
        Long id = this.coinFlipService.play(request.getChoice(), request.getBetAmount());
        return Response.created(URI.create("/game/coin-flip/" + id)).build();
    }

    public Entity result(Long id) throws Siren4JException {
        CoinFlipGame game = this.coinFlipService.findById(id)
                .orElseThrow(() -> new GameNotFoundException("No coin-flip game found with id %d".formatted(id)));

        GetCoinFlipGameResultResponse gameResultResponse = new GetCoinFlipGameResultResponse(game.id, game.getChoice(), game.getResult(),
                game.getBetAmount(), game.getGameResult());
        return ReflectingConverter.newInstance().toEntity(gameResultResponse);
    }

    public Entity game() throws Siren4JException {
        GetCoinFlipGameResponse gameResponse = new GetCoinFlipGameResponse();
        return ReflectingConverter.newInstance().toEntity(gameResponse);
    }
}