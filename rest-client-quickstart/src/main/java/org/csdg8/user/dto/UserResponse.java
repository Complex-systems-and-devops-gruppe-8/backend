package org.csdg8.user.dto;

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
@Siren4JEntity(name = "user", uri = "/users/{id}")
public class UserResponse extends BaseResource { // TODO rename to GetUserResponse
    private Long id;
    private String username;
    private String roles;
}
