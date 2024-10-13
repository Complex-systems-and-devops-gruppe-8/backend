package org.csdg8.user;

import java.net.URI;

import com.google.code.siren4j.component.Link;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

import static com.google.code.siren4j.component.builder.LinkBuilder.createLinkBuilder;

/**
 * The UserLinkBuilder class is responsible for creating new 
 * and generating common links for resources.
 * <p>
 * This class provides methods to generate links for individual resources
 * and collections of resources. It uses the UriInfo interface to build
 * URIs based on the current context of the application.
 * <p>
 * The generated links are to be compliant with the Siren hypermedia specification,
 * using the siren4j library to create Link objects.
 * <p>
 * See 
 * {@link https://github.com/sdaschner/jaxrs-hypermedia/tree/acd38c0900577123bf7bbe608754e19640d4fe22/siren-siren4j}
 * for an example implementation.
 * <p>
 * And {@link https://github.com/kevinswiber/siren} for the Siren spec.
 * 
 * @see com.google.code.siren4j.component.Link
 * @see jakarta.ws.rs.core.UriInfo
 */
@ApplicationScoped
public class UserLinkBuilder {
    private static final String SELF_REL = "self";

    public Link forUser(User user, UriInfo uriInfo) {
        return createResourceUri(UserResource.class, "getUser", user.id, uriInfo);
    }

    public Link forUsers(UriInfo uriInfo, String relation) {
        return createResourceUri(UserResource.class, relation, uriInfo);
    }

    public Link forUsers(UriInfo uriInfo) {
        return forUsers(uriInfo, SELF_REL);
    }

    private Link createResourceUri(Class<?> resourceClass, String method, long id, UriInfo uriInfo) {
        final URI uri = uriInfo.getBaseUriBuilder().path(resourceClass).path(resourceClass, method).build(id);
        return createLinkBuilder().setHref(uri.toString()).setRelationship(SELF_REL).build();
    }

    private Link createResourceUri(Class<?> resourceClass, String relation, UriInfo uriInfo) {
        final URI uri = uriInfo.getBaseUriBuilder().path(resourceClass).build();
        return createLinkBuilder().setHref(uri.toString()).setRelationship(relation).build();
    }
}
