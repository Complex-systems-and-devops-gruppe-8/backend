package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.dto.PlayCoinFlipRequest;
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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/game/coin-flip")
@Produces(Siren4J.JSON_MEDIATYPE)
public class CoinFlipResource {

    @Inject
    CoinFlipController coinFlipController;
    //TODO limit user interaction with other user's games.
    @POST
    @RolesAllowed("user")
    public Response play(PlayCoinFlipRequest request) {
        return this.coinFlipController.play(request);
    }

    @GET
    @Path("/{gameId}")
    @RolesAllowed("user")
    public Entity result(Long gameId) throws Siren4JException {
        return this.coinFlipController.result(gameId);
    }

    @GET
    @RolesAllowed("user")
    public Entity game() throws Siren4JException {
        return this.coinFlipController.game();
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(GameNotFoundException x) {
        return RestResponse.status(Response.Status.NOT_FOUND, "Coin-flip game not found");
    }
}