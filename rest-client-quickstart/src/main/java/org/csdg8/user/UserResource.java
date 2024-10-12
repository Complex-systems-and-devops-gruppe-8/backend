package org.csdg8.user;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(hal = true, path = "users")
public interface UserResource extends PanacheEntityResource<User, Long> {
    
}
