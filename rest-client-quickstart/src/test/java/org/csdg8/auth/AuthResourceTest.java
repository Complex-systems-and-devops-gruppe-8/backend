package org.csdg8.auth;

import static io.restassured.RestAssured.given;
import static org.csdg8.util.SirenAssertion.actionShouldHaveField;
import static org.csdg8.util.SirenAssertion.actionShouldHaveHref;
import static org.csdg8.util.SirenAssertion.actionShouldHaveType;
import static org.hamcrest.Matchers.equalTo;
import static org.csdg8.util.SirenAssertion.actionShouldHaveMethod;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.CreateTokenResponse;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.impl.ActionImpl.Method;
import com.google.code.siren4j.converter.ReflectingConverter;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;

@QuarkusTest
public class AuthResourceTest {

    @TestHTTPResource
    @TestHTTPEndpoint(AuthResource.class)
    URL authUrl;

    @Inject
    AuthResource authResource;

    /**
     * Configures RestAssured to use Jackson for JSON serialization/deserialization.
     * This prevents issues with XML processing.
     */
    @BeforeAll
    public static void setupAll() {
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
                            @Override
                            public ObjectMapper create(Type type, String s) {
                                return new ObjectMapper();
                            }
                        }));
    }

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
    public void shouldBeReachableViaHypermedia() {
        given()
                .when()
                .get("/")
                .then()
                .body("links.find { it.rel.contains('auth') }.href",
                        equalTo("/auth"));
    }

    @Test
    public void shouldReturnOkWhenCreatingTokenWithValidAdminCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("admin", "admin1234");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(authUrl + "/token")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnOkWhenCreatingTokenWithValidUserCreateTokenRequest() {
        CreateTokenRequest credentials = new CreateTokenRequest("user", "user1234");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(authUrl + "/token")
                .then()
                .statusCode(HttpStatus.SC_OK);
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
    public void shouldReturnOkWhenRefreshingAccessTokenWithValidAdminRefreshToken() {
        CreateTokenResponse credentials = obtainTokens("admin", "admin1234");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest(
                credentials.getRefreshToken(), credentials.getAccessToken());

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
                .contentType(ContentType.JSON)
                .body(refreshRequest)
                .when()
                .post(authUrl + "/token/refresh")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnOkAndNewAccessTokenWhenRefreshingAccessTokenWithValidUserRefreshToken() {
        CreateTokenResponse credentials = obtainTokens("user", "user1234");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest(
                credentials.getRefreshToken(), credentials.getAccessToken());

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
                .contentType(ContentType.JSON)
                .body(refreshRequest)
                .when()
                .post(authUrl + "/token/refresh")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldReturnUnauthorizedWhenRefreshingAccessTokenWithInvalidRefreshToken() {
        CreateTokenResponse credentials = obtainTokens("admin", "admin1234");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.accessToken = credentials.getAccessToken();
        String invalidRefreshToken = UUID.randomUUID().toString();
        refreshRequest.refreshToken = invalidRefreshToken;

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
        CreateTokenResponse credentials = obtainTokens("admin", "admin1234");

        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.accessToken = "invalidAccessToken";
        refreshRequest.refreshToken = credentials.getRefreshToken();

        given().header("Authorization", "Bearer " + refreshRequest.accessToken)
                .contentType(ContentType.JSON)
                .body(refreshRequest)
                .when()
                .post(authUrl + "/token/refresh")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void whenIGetTheAuthResponse() {
        Response response = given()
                .when()
                .get(authUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        itShouldHaveActionToCreateToken(response);
        itShouldHaveActionToRefreshAccessToken(response);
    }

    private void itShouldHaveActionToCreateToken(Response response) {
        String actionName = "create-token";
        actionShouldHaveMethod(response, actionName, Method.POST);
        actionShouldHaveType(response, actionName);
        actionShouldHaveHref(response, actionName, "/auth/token");
        actionShouldHaveField(response, actionName, "username", "TEXT", true);
        actionShouldHaveField(response, actionName, "password", "TEXT", true);
    }

    private void itShouldHaveActionToRefreshAccessToken(Response response) {
        String actionName = "refresh-access-token";
        actionShouldHaveMethod(response, actionName, Method.POST);
        actionShouldHaveType(response, actionName);
        actionShouldHaveHref(response, actionName, "/auth/token/refresh");
        actionShouldHaveField(response, actionName, "accessToken", "TEXT", true);
        actionShouldHaveField(response, actionName, "refreshToken", "TEXT", true);
    }

    @SneakyThrows
    private CreateTokenResponse obtainTokens(String username, String password) {
        Entity tokensResponse = this.authResource.createToken(new CreateTokenRequest(username, password));
        CreateTokenResponse tokens = (CreateTokenResponse) ReflectingConverter.newInstance().toObject(tokensResponse,
                CreateTokenResponse.class);
        assert tokens != null;
        assert tokens.getAccessToken() != null;
        assert tokens.getRefreshToken() != null;

        return tokens;
    }
}