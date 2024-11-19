package org.csdg8.game.blackjack;

import java.net.URI;

import org.csdg8.game.blackjack.dto.GetBlackjackGameResponse;
import org.csdg8.game.blackjack.dto.GetBlackjackGameResultResponse;
import org.csdg8.game.blackjack.dto.PlayBlackjackRequest;
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
public class BlackjackController {

    @Inject
    BlackjackService blackjackService ;

    public Response play(@Valid @NotNull PlayBlackjackRequest request) {
        Long id = this.blackjackService.play(request.getChoice(), request.getBetAmount());
        return Response.created(URI.create("/game/blackjack/" + id)).build();
    }

    public Entity result(Long id) throws Siren4JException {
        BlackjackGame game = this.blackjackService.findById(id)
                .orElseThrow(() -> new GameNotFoundException("No blackjack game found with id %d".formatted(id)));

        GetBlackjackGameResultResponse gameResultResponse = new GetBlackjackGameResultResponse(game.id, game.getChoice(), game.getResult(),
                game.getBetAmount(), game.getGameResult());
        return ReflectingConverter.newInstance().toEntity(gameResultResponse);
    }

    public Entity game() throws Siren4JException {
        GetBlackjackGameResponse gameResponse = new GetBlackjackGameResponse();
        return ReflectingConverter.newInstance().toEntity(gameResponse);
    }

    public Response performAction(Long gameId, String action) {
        BlackjackGame game = this.blackjackService.findById(gameId)
            .orElseThrow(() -> new GameNotFoundException("No blackjack game found with id %d".formatted(gameId)));
    
        switch (action.toLowerCase()) {
            case "hit":
                game.hit();
                break;
            case "stand":
                game.stand();
                break;
            // Add other cases
        }
    
        this.blackjackService.updateGame(game);
        return Response.ok().build();
    }

     
}