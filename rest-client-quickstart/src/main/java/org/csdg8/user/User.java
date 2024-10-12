package org.csdg8.user;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "app-user")
@UserDefinition
public class User extends PanacheEntity {

    @Username
    @NotBlank
    @Column(unique = true) // Uniqueness must be checked at DB level https://stackoverflow.com/q/3495368
    @Size(min = 3, max = 30)
    public String username;

    @Password
    @JsonIgnore
    @Size(min = 8, max = 100)
    public String password;

    @Roles
    @NotNull
    public Set<String> role;

    /**
     * Adds a new user to the database
     * 
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     * @param role     the user assigned roles
     */
    public static void add(String username, String password, Set<String> role) {
        assert username.length() != 0;
        assert password.length() != 0;
        assert !role.isEmpty();
        assert findByUsername(username).isEmpty();

        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
    }

    /**
     * Find a user by username
     * 
     * @param username the username to search for
     * @return an Optional User entity
     */
    public static Optional<User> findByUsername(String username) {
        return Optional.ofNullable(find("username", username).firstResult());
    }

    /**
     * Validate a user by their credentials
     * 
     * @param username the username
     * @param password the unencrypted password
     * @return an Optional User entity
     */
    public static Optional<User> validateUser(String username, String password) {
        Optional<User> user = findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        if (BcryptUtil.matches(password, user.get().password)) {
            return user;
        } else {
            return Optional.empty();
        }
    }
}