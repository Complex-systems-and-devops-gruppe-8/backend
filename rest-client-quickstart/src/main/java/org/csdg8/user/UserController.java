package org.csdg8.user;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.csdg8.user.dto.CollectionUserResponse;
import org.csdg8.user.dto.CreateUserRequest;
import org.csdg8.user.dto.GetUserResponse;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserController {

    @Inject
    UserService userService;

    public Response createUser(@Valid @NotNull CreateUserRequest request) {
        Long id = this.userService.addUser(request.username, request.password, Set.of("user"));
        return Response.created(URI.create("/users/" + id)).build();
    }

    public Entity getUsers() throws Siren4JException {
        List<User> users = this.userService.getAllUsers();
        List<GetUserResponse> userResponses = users.stream()
                .map(user -> new GetUserResponse(user.id, user.username, user.role.toString()))
                .collect(Collectors.toList());
        CollectionUserResponse collectionUserResponses = new CollectionUserResponse(userResponses);
        return ReflectingConverter.newInstance().toEntity(collectionUserResponses);
    }

    public Entity getUser(Long id) throws Siren4JException {
        User user = this.userService.getUser(id);
        GetUserResponse userResponse = new GetUserResponse(user.id, user.username, user.role.toString());
        return ReflectingConverter.newInstance().toEntity(userResponse);
    }
}
