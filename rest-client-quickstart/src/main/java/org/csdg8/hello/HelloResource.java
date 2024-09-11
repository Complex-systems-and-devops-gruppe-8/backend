package org.csdg8.hello;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Path("hello")
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
    public String helloAdmin() {
        return "Hello, Admin!";
    }

    @GET
    @Path("user")
    @RolesAllowed("user")
    public String helloUser(@Context SecurityContext ctx) {
        String name = ctx.getUserPrincipal().getName();
        return "Hello, " + name + "!";
    }
}