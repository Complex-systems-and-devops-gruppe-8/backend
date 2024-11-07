package org.csdg8.game.coinflip;

import org.csdg8.game.coinflip.dto.PlayCoinFlipRequest;
import org.csdg8.hello.dto.GetHelloAdminResponse;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/game/coin-flip")
@Produces(Siren4J.JSON_MEDIATYPE)
public class CoinFlipResource {

    @Inject
    CoinFlipController coinFlipController;
    //TODO add GET /game/coin-fip with actions to play and get result
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
}