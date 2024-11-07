package org.csdg8.hello;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.net.URL;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.csdg8.auth.AuthResource;
import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.CreateTokenResponse;
import org.csdg8.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;

@QuarkusTest
public class HelloResourceTest {

    @TestHTTPEndpoint(HelloResource.class)
    @TestHTTPResource
    URL url;

    @Inject
    AuthResource authResource;

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
    public void shouldReturnOkAndHelloWorldWhenAccessingAllEndpointAnonymously() {
        given()
                .when().get(this.url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, World!"));
    }

    @Test
    void shouldReturnUnauthorizedWhenAccessingAdminEndpointAnonymously() {
        get(this.url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void shouldReturnOkWhenAccessingAdminEndpointAsAdmin() {
        String adminToken = obtainTokens("admin", "admin1234").getAccessToken();

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldReturnOkWhenAccessingAllEndpointAsAdmin() {
        String adminToken = obtainTokens("admin", "admin1234").getAccessToken();

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldReturnForbiddenWhenAccessingUserEndpointAsAdmin() {
        String adminToken = obtainTokens("admin", "admin1234").getAccessToken();

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void shouldReturnOkAndUserIdentityWhenAccessingUserEndpointAsUser() {
        String username = "user";
        String userToken = obtainTokens(username, "user1234").getAccessToken();

        given().header("Authorization", "Bearer " + userToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, " + username + "!"));
    }

    @SneakyThrows
    private CreateTokenResponse obtainTokens(String username, String password) {
        Entity tokensResponse = authResource.createToken(new CreateTokenRequest(username, password));
        CreateTokenResponse tokens = (CreateTokenResponse) ReflectingConverter.newInstance().toObject(tokensResponse,
                CreateTokenResponse.class);
        assert tokens != null;
        assert tokens.getAccessToken() != null;
        assert tokens.getRefreshToken() != null;

        return tokens;
    }
}
