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
@Siren4JEntity(entityClass = "refresh-access-token", uri = "/auth/refresh")
public class RefreshAccessTokenResponse extends BaseResource {
    private String accessToken;
}
