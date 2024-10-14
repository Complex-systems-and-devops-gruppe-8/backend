package org.csdg8.auth;

import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.CreateTokenResponse;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.auth.dto.RefreshAccessTokenResponse;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    public Entity createToken(@Valid @NotNull CreateTokenRequest request) throws Siren4JException {
        String accessToken = this.authService.createAccessToken(request.getUsername(), request.getPassword());
        String refreshToken = this.authService.createRefreshToken(request.getUsername(), request.getPassword());

        CreateTokenResponse createTokenResponse = new CreateTokenResponse(accessToken, refreshToken);
        return ReflectingConverter.newInstance().toEntity(createTokenResponse);
    }

    public Entity refreshAccessToken(@Valid @NotNull RefreshAccessTokenRequest request) throws Siren4JException {
        String newAccessToken = this.authService.refreshAccessToken(request.refreshToken, request.accessToken);

        RefreshAccessTokenResponse refreshAccessTokenResponse = new RefreshAccessTokenResponse(newAccessToken);
        return ReflectingConverter.newInstance().toEntity(refreshAccessTokenResponse);
    }
}
