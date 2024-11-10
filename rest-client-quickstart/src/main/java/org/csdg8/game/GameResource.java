package org.csdg8.game;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/game")
@Produces(Siren4J.JSON_MEDIATYPE)
@RolesAllowed({"user", "admin"})
public class GameResource {

    @Inject
    GameController gameController;

    @GET
    public Entity getGame() throws Siren4JException {
        return this.gameController.getGame();
    }
}
