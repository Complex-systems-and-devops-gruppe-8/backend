package org.csdg8.game.coinflip;

import java.net.URI;

import org.csdg8.game.coinflip.dto.GetCoinFlipGameResponse;
import org.csdg8.game.coinflip.dto.PlayCoinFlipRequest;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CoinFlipController {

    public Response play(@Valid @NotNull PlayCoinFlipRequest request) {
        Long id = CoinFlipGame.play(request.getChoice(), request.getBetAmount());
        //TODO add game to user's list of games
        //TODO pay / subtract money from user
        return Response.created(URI.create("/game/coin-flip/" + id)).build();
    }

    public Entity result(Long id) throws Siren4JException {
        CoinFlipGame game = CoinFlipGame.findById(id);
        GetCoinFlipGameResponse gameResponse = new GetCoinFlipGameResponse(game.id, game.choice, game.result, game.betAmount, game.gameResult);
        return ReflectingConverter.newInstance().toEntity(gameResponse);
    }
}