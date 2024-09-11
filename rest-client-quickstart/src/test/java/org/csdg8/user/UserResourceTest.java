package org.csdg8.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.user.UserResource.RegistrationRequest;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class UserResourceTest {

    @TestHTTPEndpoint(UserResource.class)
    @TestHTTPResource
    URL usersUrl;

    @Test
    public void testSuccessfulUserRegistration() {
        RegistrationRequest request = new RegistrationRequest("John", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(this.usersUrl)
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
                .post(this.usersUrl);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(this.usersUrl)
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
                .post(this.usersUrl)
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
                .post(this.usersUrl)
                .then()
                .statusCode(400);
    }
}
