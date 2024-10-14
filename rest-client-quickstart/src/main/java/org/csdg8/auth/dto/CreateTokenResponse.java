package org.csdg8.auth.dto;

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
@Siren4JEntity(entityClass = "access-token", uri = "/auth/token")
public class CreateTokenResponse extends BaseResource {
    private String accessToken;
    private String refreshToken;
}
