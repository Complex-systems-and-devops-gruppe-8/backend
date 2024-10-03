package org.csdg8.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.user.dto.RegistrationRequest;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class UserResourceTest {

    @TestHTTPEndpoint(UserResource.class)
    @TestHTTPResource
    URL url;

    @Test
    public void shouldReturnCreatedWhenRegisteringValidUser() {
        RegistrationRequest request = new RegistrationRequest("john", "password123");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void shouldReturnConflictWhenRegisteringExistingUser() {
        RegistrationRequest request = new RegistrationRequest("admin", "password123");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void shouldReturnBadRequestWhenRegisteringWithInvalidCredentials() {
        RegistrationRequest request = new RegistrationRequest("u", "short");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(url)
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnOKWhenGettingAllUsers() {
        given()
        .when()
            .get(url)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(ContentType.JSON)
            .body(not(empty()));
    }

    @Test
    public void shouldReturnOKWhenGettingExistingUser() {
        given()
        .when()
            .get(url + "/user")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(ContentType.JSON)
            .body(not(empty()));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenGettingNonExistentUser() {
        given()
        .when()
            .get(url + "/nonexistentuser")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
