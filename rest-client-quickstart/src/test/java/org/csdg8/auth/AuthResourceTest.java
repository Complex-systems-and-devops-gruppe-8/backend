package org.csdg8.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

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
    public void testRefreshAccessTokenWithValidAdminRefreshToken() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("admin", "admin");
        ExtractableResponse<io.restassured.response.Response> tokens = given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract();

        String refreshToken = tokens.path("refreshToken");
        String accessToken = tokens.path("accessToken");

        AuthResource.RefreshAccessTokenRequest refreshRequest = new AuthResource.RefreshAccessTokenRequest();
        refreshRequest.accessToken = accessToken;
        refreshRequest.refreshToken = refreshToken;

        given().header("Authorization", "Bearer " + accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void testRefreshAccessTokenWithValidUserRefreshToken() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("user", "user");
        ExtractableResponse<Response> tokens = given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract();

            String refreshToken = tokens.path("refreshToken");
            String accessToken = tokens.path("accessToken");
    
            AuthResource.RefreshAccessTokenRequest refreshRequest = new AuthResource.RefreshAccessTokenRequest();
            refreshRequest.accessToken = accessToken;
            refreshRequest.refreshToken = refreshToken;

        given().header("Authorization", "Bearer " + accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void testRefreshAccessTokenWithInvalidRefreshToken() {
        AuthResource.Credentials credentials = new AuthResource.Credentials("admin", "admin");
        String accessToken = given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("accessToken");

        AuthResource.RefreshAccessTokenRequest refreshRequest = new AuthResource.RefreshAccessTokenRequest();
        refreshRequest.accessToken = accessToken;
        refreshRequest.refreshToken = "invalidRefreshToken";

        given().header("Authorization", "Bearer " + accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}