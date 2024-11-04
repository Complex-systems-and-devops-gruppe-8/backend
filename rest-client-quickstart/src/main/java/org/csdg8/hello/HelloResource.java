package org.csdg8.hello;

import org.csdg8.hello.dto.GetHelloAdminResponse;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("hello")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
public class HelloResource {

    @GET
    @Path("all")
    @PermitAll
    public String hello() {
        return "Hello, World!";
    }

    @GET
    @Path("admin")
    @RolesAllowed("admin")
    @Produces(Siren4J.JSON_MEDIATYPE)
    public Entity helloAdmin() throws Siren4JException {
        GetHelloAdminResponse rootResponse = new GetHelloAdminResponse();
        rootResponse.setMessage("Hello, Admin!");
        return ReflectingConverter.newInstance().toEntity(rootResponse);
    }

    @GET
    @Path("user")
    @RolesAllowed("user")
    public String helloUser(@Context SecurityContext ctx) {
        String name = ctx.getUserPrincipal().getName();
        return "Hello, " + name + "!";
    }
}