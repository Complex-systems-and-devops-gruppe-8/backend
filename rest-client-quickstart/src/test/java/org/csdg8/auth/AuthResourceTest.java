package org.csdg8.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.net.URL;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

@QuarkusTest
public class AuthResourceTest {

    @TestHTTPResource
    @TestHTTPEndpoint(AuthResource.class)
    URL authUrl;

    @BeforeAll
    @Transactional
    public static void setup() {
        User.add("admin", "admin1234", Set.of("admin"));
        User.add("user", "user1234", Set.of("user"));
    }

    @AfterAll
    @Transactional
    public static void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldReturnOkAndTokensWhenCreatingTokenWithValidAdminCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin1234");

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
        CreateTokenRequest credentials = new CreateTokenRequest("user", "user1234");

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
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin1234");
        RefreshAccessTokenRequest tokens = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().as(RefreshAccessTokenRequest.class);

        given().header("Authorization", "Bearer " + tokens.accessToken)
            .contentType(ContentType.JSON)
            .body(tokens)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void shouldReturnOkAndNewAccessTokenWhenRefreshingAccessTokenWithValidUserRefreshToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("user", "user1234");
        RefreshAccessTokenRequest tokens = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().as(RefreshAccessTokenRequest.class);

        given().header("Authorization", "Bearer " + tokens.accessToken)
            .contentType(ContentType.JSON)
            .body(tokens)
            .when()
            .post(authUrl + "/token/refresh")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("accessToken", notNullValue());
    }

    @Test
    public void shouldReturnUnauthorizedWhenRefreshingAccessTokenWithInvalidRefreshToken() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin1234");
        String accessToken = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .as(RefreshAccessTokenRequest.class)
            .accessToken;

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
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin1234");
        String refreshToken = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(credentials)
            .when()
            .post(authUrl + "/token")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().as(RefreshAccessTokenRequest.class)
            .refreshToken;

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