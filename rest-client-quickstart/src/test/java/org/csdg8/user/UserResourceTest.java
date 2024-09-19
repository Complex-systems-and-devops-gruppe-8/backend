package org.csdg8.user;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.user.UserResource.RegistrationRequest;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

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
    public void testInvalidUsername() {
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
    public void testInvalidPassword() {
        RegistrationRequest request = new RegistrationRequest("validuser", "short");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(this.usersUrl)
                .then()
                .statusCode(400);
    }

    @Test
    public void testAllUsers() {
        when()
                .get(usersUrl)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testOneUser() {
        when()
            .get(usersUrl + "/user")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testFailOneUser() {
        when()
            .get(usersUrl + "/nonexistinguser")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
