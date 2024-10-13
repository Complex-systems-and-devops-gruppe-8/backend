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

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

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
        String adminToken = obtainToken("admin", "admin1234");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldReturnOkWhenAccessingAllEndpointAsAdmin() {
        String adminToken = obtainToken("admin", "admin1234");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldReturnForbiddenWhenAccessingUserEndpointAsAdmin() {
        String adminToken = obtainToken("admin", "admin1234");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void shouldReturnOkAndUserIdentityWhenAccessingUserEndpointAsUser() {
        String username = "user";
        String userToken = obtainToken(username, "user1234");

        given().header("Authorization", "Bearer " + userToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, " + username + "!"));
    }

    private String obtainToken(String username, String password) {
        Response response = authResource.createToken(new CreateTokenRequest(username, password));
        CreateTokenResponse tokenResponse = response.readEntity(CreateTokenResponse.class);
        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new RuntimeException(String.format(
                    "Failed to obtain token for user: %s"
                            + " with password: %s"
                            + ". Response: %s",
                    username, password, response.readEntity(String.class)));
        }

        return tokenResponse.accessToken;
    }
}
