package org.csdg8.user.dto;

import java.util.Collection;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.resource.CollectionResource;

@Siren4JEntity(name = "users", uri = "/users")
public class GetCollectionUserResponse extends CollectionResource<GetUserResponse> {
    public GetCollectionUserResponse(Collection<GetUserResponse> items) {
        setItems(items);
    }
}
