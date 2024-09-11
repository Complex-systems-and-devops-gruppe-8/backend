package org.csdg8.hello;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.auth.AuthResource;
import org.csdg8.auth.AuthResource.CreateTokenResponse;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class HelloResourceTest {

    @TestHTTPEndpoint(HelloResource.class)
    @TestHTTPResource
    URL url;

    @Inject
    AuthResource authResource;

    @Test
    public void shouldReturnHelloWorldWhenAccessingAllEndpoint() {
        given()
                .when().get(this.url + "/all")
                .then()
                .statusCode(200)
                .body(is("Hello, World!"));
    }

    @Test
    void shouldAccessPublicWhenAnonymous() {
        get(this.url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldNotAccessAdminWhenAnonymous() {
        get(this.url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void shouldAccessAdminWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldAccessAllWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldNotAccessUserWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        String username = "user";
        String userToken = obtainToken(username, "user");

        given().header("Authorization", "Bearer " + userToken)
                .when()
                .get(this.url + "/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, " + username + "!"));
    }

    private String obtainToken(String username, String password) {
        Response response = authResource.createToken(new AuthResource.Credentials(username, password));
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
