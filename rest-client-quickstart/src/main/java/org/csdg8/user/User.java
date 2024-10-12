package org.csdg8.user;

import java.util.Optional;
import java.util.Set;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "app-user")
@UserDefinition
public class User extends PanacheEntity {
    @Username
    public String username;
    @Password
    public String password;
    @Roles
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
}