package org.csdg8.user;

import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
        UserResponse[] userResponses = users.stream()
                .map(this::mapToUserResponse)
                .toArray(UserResponse[]::new);
        return Response.ok(userResponses).build();
    }

    public Response one(String username) {
        User user = this.userService.getUser(username);
        return Response.ok(new UserResponse(user.id, user.username, user.role)).build();
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.id, user.username, user.role);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistrationRequest {
        public String username;
        public String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        public Long id;
        public String username;
        public Set<String> role;
    }
}
