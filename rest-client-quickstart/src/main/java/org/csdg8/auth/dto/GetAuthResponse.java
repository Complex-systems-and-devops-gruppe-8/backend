package org.csdg8.auth.dto;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;

import jakarta.ws.rs.core.MediaType;

@Siren4JEntity(entityClass = "auth", uri = "/auth", actions = {
        @Siren4JAction(name = "create-token", title = "Create token", method = Method.POST, href = "/auth/token", type = MediaType.APPLICATION_JSON, fields = {
                @Siren4JActionField(name = "username", title = "Username", required = true, type = "text"),
                @Siren4JActionField(name = "password", title = "Plain-text password", required = true, type = "text")
        }),
        @Siren4JAction(name = "refresh-access-token", title = "Refresh access token", method = Method.POST, href = "/auth/token/refresh", type = MediaType.APPLICATION_JSON, fields = {
                @Siren4JActionField(name = "accessToken", title = "Access token", required = true, type = "text"),
                @Siren4JActionField(name = "refreshToken", title = "Refresh token", required = true, type = "text")
        })
})
public class GetAuthResponse {
}
