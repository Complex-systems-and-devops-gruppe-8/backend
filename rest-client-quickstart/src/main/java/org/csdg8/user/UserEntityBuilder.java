package org.csdg8.user;

import java.util.List;
import java.util.stream.Collectors;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.impl.ActionImpl;
import com.google.code.siren4j.meta.FieldType;

import static com.google.code.siren4j.component.builder.ActionBuilder.createActionBuilder;
import static com.google.code.siren4j.component.builder.EntityBuilder.createEntityBuilder;
import static com.google.code.siren4j.component.builder.FieldBuilder.createFieldBuilder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

/**
 * The UserEntityBuilder class is responsible for constructing Siren entities
 * representing resources.
 * <p>
 * This class provides methods to build entity representations for both
 * individual resources and collections of resources. It uses the siren4j
 * library to create Entity objects that conform to the Siren hypermedia
 * specification.
 * <p>
 * The builder includes functionality to add properties, links, and actions
 * to the entities, providing a rich hypermedia representation of the resources.
 * <p>
 * See
 * {@link https://github.com/sdaschner/jaxrs-hypermedia/tree/acd38c0900577123bf7bbe608754e19640d4fe22/siren-siren4j}
 * for an example implementation.
 * <p>
 * And {@link https://github.com/kevinswiber/siren} for the Siren spec.
 *
 * @see com.google.code.siren4j.component.Entity
 * @see com.google.code.siren4j.component.Action
 * @see com.google.code.siren4j.component.Field
 * @see jakarta.ws.rs.core.UriInfo
 */
@ApplicationScoped
public class UserEntityBuilder {

    @Inject
    UserLinkBuilder userLinkBuilder;

    public Entity buildUsers(List<User> users, UriInfo uriInfo) {
        final List<Entity> userEntities = users.stream()
                .map(user -> buildUserSub(user, uriInfo, "item"))
                .collect(Collectors.toList());

        return createEntityBuilder().setComponentClass("users")
                .addAction(createUser(uriInfo))
                .addLink(userLinkBuilder.forUsers(uriInfo))
                .addSubEntities(userEntities).build();
    }

    private Entity buildUserSub(User user, UriInfo uriInfo, String rel) {
        return createEntityBuilder().setRelationship(rel)
                .addLink(userLinkBuilder.forUser(user, uriInfo))
                .addProperty("id", user.id)
                .addProperty("username", user.username)
                .addProperty("roles", user.role)
                .build();
    }

    public Entity buildUser(User user, UriInfo uriInfo) {
        final EntityBuilder entityBuilder = createEntityBuilder().setComponentClass("user")
                .addLink(userLinkBuilder.forUser(user, uriInfo))
                .addProperty("id", user.id)
                .addProperty("username", user.username)
                .addProperty("roles", user.role);

        return entityBuilder.build();
    }

    private Action createUser(UriInfo uriInfo) {
        return createActionBuilder().setName("create")
                .setTitle("Create a new user")
                .setMethod(ActionImpl.Method.POST)
                .setHref(userLinkBuilder.forUsers(uriInfo).getHref())
                .setType(MediaType.APPLICATION_JSON)
                .addField(
                        createFieldBuilder()
                                .setName("username")
                                .setType(FieldType.TEXT)
                                .setRequired(true)
                                .setMaxLength(50)
                                .build())
                .addField(
                        createFieldBuilder()
                                .setName("roles")
                                .setType(FieldType.TEXT)
                                .setRequired(true)
                                .build())
                .build();
    }
}
