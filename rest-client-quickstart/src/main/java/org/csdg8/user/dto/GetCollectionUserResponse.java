package org.csdg8.user.dto;

import java.util.Collection;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;
import com.google.code.siren4j.resource.CollectionResource;

import jakarta.ws.rs.core.MediaType;

@Siren4JEntity(name = "users", uri = "/users", actions = {
        @Siren4JAction(name = "create-user", title = "Create user", method = Method.POST, href = "/users", type = MediaType.APPLICATION_JSON, fields = {
                @Siren4JActionField(name = "username", title = "Username", required = true, type = "text"),
                @Siren4JActionField(name = "password", title = "Plain-text password", required = true, type = "text")
        })
})
public class GetCollectionUserResponse extends CollectionResource<GetUserResponse> {
    public GetCollectionUserResponse(Collection<GetUserResponse> items) {
        setItems(items);
    }
}
