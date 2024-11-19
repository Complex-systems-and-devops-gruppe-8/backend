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
        assert username != null;
        assert password != null;

        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        if (BcryptUtil.matches(password, user.get().getPassword())) {
            return user;
        } else {
            return Optional.empty();
        }
    }

    public Optional<Set<String>> getUserRole(String username) {
        assert username != null;

        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        Set<String> role = user.get().getRole();

        return Optional.of(role);
    }

    public Optional<User> findByUsername(String username) {
        assert username != null;

        Optional<User> user = User.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return user;
    }

    @Transactional
    public Long addUser(String username, String password, Set<String> roles) {
        assert username != null;
        assert password != null;
        assert roles != null;

        if (!isValidUsername(username) || !isValidPassword(password)) {
            throw new InvalidCredentialsException("Failed to add user %s as the credentials were not valid");
        }

        if (this.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Failed to add user %s as they already exist");
        }
        return User.add(username, password, roles);
    }

    private boolean isValidUsername(String username) {
        assert username != null;

        String validChars = "a-zA-Z0-9_";
        int minLength = 3;
        int maxLength = 30;

        String regex = "^[%s]{%d,%d}$".formatted(validChars, minLength, maxLength);
        return username.matches(regex);
    }

    private boolean isValidPassword(String password) {
        assert password != null;

        return password != null && password.length() >= 8 && password.length() < 50;
    }

    public User getUser(Long id) {
        assert id != null;

        Optional<User> optUser = Optional.ofNullable(User.findById(id));

        return optUser.orElseThrow(
            () -> new UserNotFoundException("No user found with id %d".formatted(id)));
    }

    public List<User> getAllUsers() {
        return User.listAll();
    }

    @Transactional
    public void addGameToUser(Long userId, Long gameId) {
        assert userId != null;
        assert gameId != null;

        User user = getUser(userId);

        user.getLinkedGames().add(gameId);
        user.persist();
    }

    @Transactional
    public void addBalance(Long userId, Integer value) {
        assert userId != null;
        assert value != null;
        assert value > 0;

        User user = getUser(userId);
        Integer currentBalance = user.getBalance();
        Integer newBalance = currentBalance + value;

        assert newBalance > 0;
        assert newBalance > currentBalance;
        user.setBalance(newBalance);
        user.persist();
    }

    @Transactional
    public void removeBalance(Long userId, Integer value) {
        assert userId != null;
        assert value != null;
        assert value > 0;
        
        User user = getUser(userId);
        Integer currentBalance = user.getBalance();
        Integer newBalance = currentBalance - value;

        assert newBalance >= 0;
        assert currentBalance > newBalance;
        user.setBalance(newBalance);
        user.persist();
    }

    @Transactional
    public Set<Long> getUserGames(Long userId) {
        assert userId != null;

        User user = getUser(userId);
        return user.getLinkedGames();
    }
}
