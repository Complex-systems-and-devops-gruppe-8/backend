package org.csdg8.user;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.csdg8.user.dto.RegistrationRequest;

import com.google.code.siren4j.component.Entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
public class UserController {

    @Context
    UriInfo uriInfo;

    @Inject
    UserEntityBuilder userEntityBuilder;

    @Inject
    UserService userService;

    public Response register(RegistrationRequest request) {
        Long id = this.userService.addUser(request.username, request.password, Set.of("user"));
        return Response.created(URI.create("/users/" + id)).build();
    }

    public Entity getUsers() {
        List<User> users = this.userService.getAllUsers();
        return userEntityBuilder.buildUsers(users, uriInfo);
    }

    public Entity getUser(String username) {
        User user = this.userService.getUser(username);
        return userEntityBuilder.buildUser(user, uriInfo);
    }
}
