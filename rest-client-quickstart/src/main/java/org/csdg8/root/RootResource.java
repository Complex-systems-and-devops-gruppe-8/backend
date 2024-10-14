package org.csdg8.root;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
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
