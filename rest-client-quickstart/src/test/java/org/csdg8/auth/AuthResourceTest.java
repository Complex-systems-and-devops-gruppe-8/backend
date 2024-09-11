package org.csdg8.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class AuthResourceTest {

    @TestHTTPResource
    @TestHTTPEndpoint(AuthResource.class)
    URL authUrl;

    @Test
    public void testCreateTokenWithValidAdminCredentials() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("admin", "admin");

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    public void testCreateTokenWithValidUserCredentials() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("user", "user");

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    public void testCreateTokenWithInvalidCredentials() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("invalidUser", "invalidPassword");

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void testRefreshAccessTokenWithInvalidRefreshToken() {
        AuthResource.RefreshAccessTokenRequest refreshRequest = new AuthResource.RefreshAccessTokenRequest();
        refreshRequest.username = "admin";
        refreshRequest.refreshToken = "invalidRefreshToken";

        given()
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}