package org.csdg8.infrastructure;

import java.util.Set;

import org.csdg8.user.User;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Transactional
    @IfBuildProfile(allOf = { "dev" })
    public void loadDevUsers(@Observes StartupEvent evt) {
        User.deleteAll();
        User.add("admin", "admin1234", Set.of("admin"));
        User.add("user", "user1234", Set.of("user"));
    }
}