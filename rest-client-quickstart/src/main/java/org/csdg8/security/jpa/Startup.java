package org.csdg8.security.jpa;

import java.util.List;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Transactional
    @IfBuildProfile(allOf = { "dev", "test" })
    public void loadUsers(@Observes StartupEvent evt) {
        User.deleteAll();
        User.add("admin", "admin", List.of("admin"));
        User.add("user", "user", List.of("user"));
    }
}
