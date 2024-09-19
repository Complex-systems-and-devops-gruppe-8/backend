package org.csdg8.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
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
    public void shouldReturnOkAndTokensWhenCreatingTokenWithValidAdminCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin");

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
    public void shouldReturnOkAndTokensWhenCreatingTokenWithValidUserCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("user", "user");

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
    public void shouldReturnUnauthorizedWhenCreatingTokenWithInvalidCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("invalidUser", "invalidPassword");

        given()
            .contentType(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldReturnOkAndNewAccessTokenWhenRefreshingAccessTokenWithValidAdminRefreshToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin");
        ExtractableResponse<io.restassured.response.Response> tokens = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract();

        String refreshToken = tokens.path("refreshToken");
        String accessToken = tokens.path("accessToken");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.accessToken = accessToken;
        refreshRequest.refreshToken = refreshToken;

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void shouldReturnOkAndNewAccessTokenWhenRefreshingAccessTokenWithValidUserRefreshToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("user", "user");
        ExtractableResponse<Response> tokens = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract();

            String refreshToken = tokens.path("refreshToken");
            String accessToken = tokens.path("accessToken");
    
            RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
            refreshRequest.accessToken = accessToken;
            refreshRequest.refreshToken = refreshToken;

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void shouldReturnUnauthorizedWhenRefreshingAccessTokenWithInvalidRefreshToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin");
        String accessToken = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("accessToken");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.accessToken = accessToken;
        refreshRequest.refreshToken = "invalidRefreshToken";

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnauthorizedWhenRefreshingAccessTokenWithInvalidAccessToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin");
        String refreshToken = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("refreshToken");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.accessToken = "invalidAccessToken";
        refreshRequest.refreshToken = refreshToken;

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
            .contentType(ContentType.JSON)
            .body(refreshRequest)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}