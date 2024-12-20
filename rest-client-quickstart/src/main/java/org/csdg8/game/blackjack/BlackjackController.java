package org.csdg8.game.blackjack;

import java.net.URI;

import org.csdg8.game.blackjack.dto.BlackjackActionRequest;
import org.csdg8.game.blackjack.dto.BlackjackStateResponse;
import org.csdg8.game.blackjack.dto.GetBlackjackGameResponse;
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

    public Response startGame(@Valid @NotNull PlayBlackjackRequest request) {
        BlackjackGame game = this.blackjackService.createGame(request.getBetAmount());
        //here i need to addd a DTO that has the response body of the created game:=)
        BlackjackStateResponse Blackjack = new BlackjackStateResponse(game.getDealerHand(), game.getPlayerHand(), game.getBetAmount(), game.id, game.getGameResult());
        return Response.created(URI.create("/game/blackjack/" + game.id)).entity(Blackjack).build();
    }

    
    public Entity game() throws Siren4JException {
        GetBlackjackGameResponse gameResponse = new GetBlackjackGameResponse();
        return ReflectingConverter.newInstance().toEntity(gameResponse);
    }

    public Response performAction(long gameId, @Valid @NotNull BlackjackActionRequest request) {
        // Fetch the game by its ID
        BlackjackGame game = this.blackjackService.findById(gameId)
            .orElseThrow(() -> new GameNotFoundException("No blackjack game found with id %d".formatted(gameId)));
        
      
        // Perform the requested action (HIT or STAND)
        switch (request.getChoice()) {
            case HIT -> {
                game = this.blackjackService.hit(game); // Player hits
            }
            case STAND -> {
                game = this.blackjackService.stand(game); // Player stands
            }
            default -> {
                throw new IllegalArgumentException("Invalid action: " + request.getChoice());
            }
        }
    
        // Prepare a response with the updated game state
        BlackjackStateResponse blackjackState = new BlackjackStateResponse(
            game.getDealerHand(),
            game.getPlayerHand(),
            game.getBetAmount(),
            gameId, // Ensure `game.id` is correctly referenced as `getId()`
            game.getGameResult()
        );
    
        // Return a Response with the created status and the updated game state
        return Response.created(URI.create("/game/blackjack/" + gameId))
            .entity(blackjackState)
            .build();
    }
    
      

     
}