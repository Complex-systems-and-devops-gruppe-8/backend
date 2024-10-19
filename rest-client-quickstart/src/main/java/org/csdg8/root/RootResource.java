package org.csdg8.root;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
@Produces(Siren4J.JSON_MEDIATYPE)
public class RootResource {

    @Inject
    RootController rootController;

    @GET
    @Operation(summary = "Get root", description = "Retrieves a list of all available sub-resources")
    @APIResponse(responseCode = "200", description = "Successfully retrieved root")
    public Entity getRoot() throws Siren4JException {
        return this.rootController.getRoot();
    }
}
