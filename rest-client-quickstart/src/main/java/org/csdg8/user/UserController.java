package org.csdg8.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.csdg8.user.dto.RegistrationRequest;
import org.csdg8.user.dto.UserResponse;
import org.csdg8.user.dto.UserResponseList;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserController {

    @Inject
    UserService userService;

    public Response register(RegistrationRequest request) {
        this.userService.addUser(request.username, request.password, Set.of("user"));
        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully")
                .build();
    }

    public Response all() {
        List<User> users = this.userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        UserResponseList responseList = new UserResponseList(userResponses);
        return Response.ok(responseList).build();
    }

    public Response one(String username) {
        User user = this.userService.getUser(username);
        return Response.ok(new UserResponse(user.id, user.username, user.role)).build();
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.id, user.username, user.role);
    }
}
