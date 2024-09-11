package org.csdg8.security.jpa;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.HelloResource;
import org.csdg8.auth.AuthResource;
import org.csdg8.auth.AuthResource.CreateTokenResponse;
import org.csdg8.user.RegisterResource;
import org.csdg8.user.RegisterResource.RegistrationRequest;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class JpaSecurityRealmTest {

    @TestHTTPEndpoint(HelloResource.class)
    @TestHTTPResource
    URL helloUrl;

    @TestHTTPEndpoint(AuthResource.class)
    @TestHTTPResource
    URL authUrl;

    @TestHTTPEndpoint(RegisterResource.class)
    @TestHTTPResource
    URL registerUrl;

    @Inject
    AuthResource authResource;

    @Test
    void shouldAccessPublicWhenAnonymous() {
        get(helloUrl + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldNotAccessAdminWhenAnonymous() {
        get(helloUrl + "/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void shouldAccessAdminWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(helloUrl + "/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldAccessAllWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(helloUrl + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldNotAccessUserWhenAdminAuthenticated() {
        String adminToken = obtainToken("admin", "admin");

        given().header("Authorization", "Bearer " + adminToken)
                .when()
                .get(helloUrl + "/user")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        String username = "user";
        String userToken = obtainToken(username, "user");

        given().header("Authorization", "Bearer " + userToken)
                .when()
                .get(helloUrl + "/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, " + username + "!"));
    }

    @Test
    public void testSuccessfulUserRegistration() {
        RegistrationRequest request = new RegistrationRequest("John", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(registerUrl + "/register")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(is("User registered successfully"));
    }

    @Test
    public void testDuplicateUsername() {
        RegistrationRequest request = new RegistrationRequest("John", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(registerUrl + "/register");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(registerUrl + "/register")
                .then()
                .statusCode(409)
                .body(is("Username already exists"));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        RegistrationRequest request = new RegistrationRequest("a", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(registerUrl + "/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void testInvalidPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest("validuser", "short");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(registerUrl + "/register")
                .then()
                .statusCode(400);
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
