package org.csdg8.user.dto;

import java.util.Set;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.resource.BaseResource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Siren4JEntity(entityClass = "user", uri = "/users/{id}")
public class GetUserResponse extends BaseResource {
    private Long id;
    private String username;
    private String roles;
    private Set<Long> linkedGames;
}
