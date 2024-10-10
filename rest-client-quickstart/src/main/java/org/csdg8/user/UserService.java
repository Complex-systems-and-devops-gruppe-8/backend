package org.csdg8.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    public Optional<User> validateUser(String username, String password) {
        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        if (BcryptUtil.matches(password, user.get().password)) {
            return user;
        } else {
            return Optional.empty();
        }
    }

    public Optional<Set<String>> getUserRole(String username) {
        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        Set<String> role = user.get().role;

        return Optional.of(role);
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return user;
    }

    @Transactional
    public void addUser(String username, String password, Set<String> roles) {
        if (!isValidUsername(username) || !isValidPassword(password)) {
            throw new InvalidCredentialsException();
        }

        if (this.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User.add(username, password, roles);
    }

    private boolean isValidUsername(String username) {
        String validChars = "a-zA-Z0-9_";
        int minLength = 3;
        int maxLength = 30;

        String regex = "^[%s]{%d,%d}$".formatted(validChars, minLength, maxLength);
        return username != null && username.matches(regex);
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.length() < 50;
    }

    public User getUser(String username) {
        return this.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAllUsers() {
        return User.listAll();
    }
}
