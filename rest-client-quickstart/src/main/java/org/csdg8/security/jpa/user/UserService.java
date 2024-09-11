package org.csdg8.security.jpa.user;

import java.util.Optional;
import java.util.Set;

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
        User.add(username, password, roles);
    }
}