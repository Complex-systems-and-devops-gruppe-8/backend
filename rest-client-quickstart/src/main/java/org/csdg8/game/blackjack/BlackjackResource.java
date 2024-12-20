package org.csdg8.game.blackjack;

import org.csdg8.game.blackjack.dto.BlackjackActionRequest;
import org.csdg8.game.blackjack.dto.PlayBlackjackRequest;
import org.csdg8.model.exception.GameNotFoundException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/game/blackjack")
@Produces(Siren4J.JSON_MEDIATYPE)
public class BlackjackResource {

    @Inject
    BlackjackController blackjackController;
    //TODO limit user interaction with other user's games.
    @POST
    @RolesAllowed("user")
    public Response startGame(PlayBlackjackRequest request) {
        return this.blackjackController.startGame(request);
    }

    
    @POST
    @Path("/{gameId}")
    @RolesAllowed("user")
    public Response performAction(@PathParam("gameId") Long gameId,BlackjackActionRequest request)  {
        return this.blackjackController.performAction(gameId, request);
    }   

    @GET
    @RolesAllowed("user")
    public Entity game() throws Siren4JException {
        return this.blackjackController.game();
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(GameNotFoundException x) {
        return RestResponse.status(Response.Status.NOT_FOUND, "Blackjack game not found");
    }
}