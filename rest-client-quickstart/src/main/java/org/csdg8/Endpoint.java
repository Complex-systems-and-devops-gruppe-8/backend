package org.csdg8;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("hello")
public class Endpoint {

    @GET
    public String hello() {
        return "Hello, World!";
    }
}