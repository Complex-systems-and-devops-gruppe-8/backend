package org.csdg8.root.dto;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JLink;

@Siren4JEntity(entityClass = "root", uri = "/", links = {
        @Siren4JLink(rel = "users", href = "/users")
})
public class GetRootResponse {
}
