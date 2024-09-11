package org.csdg8.security.jpa;

import java.util.Set;

import org.csdg8.security.jpa.user.User;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Transactional
    @IfBuildProfile(allOf = { "dev", "test" })
    public void loadTestDevUsers(@Observes StartupEvent evt) {
        User.deleteAll();
        User.add("admin", "admin", Set.of("admin"));
        User.add("user", "user", Set.of("user"));
    }
}
