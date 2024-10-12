package org.csdg8.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import java.util.Set;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

@QuarkusTest
public class UserResourceTest {

    final static String PATH = "/users";

    @BeforeEach
    @Transactional
    public void setup() {
        User.add("admin", "admin1234", Set.of("admin"));
        User.add("user", "user1234", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldReturnCreatedWhenRegisteringValidUser() {
        String request = """
            {
                "username": "john",
                "password": "password123",
                "role": ["user"]
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(PATH)
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void shouldReturnConflictWhenRegisteringExistingUser() {
        String request = """
            {
                "username": "admin",
                "password": "password123",
                "role": ["user"]
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(PATH)
        .then()
            .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void shouldReturnBadRequestWhenRegisteringWithInvalidCredentials() {
        String request = """
            {
                "username": "u",
                "password": "short",
                "role": ["user"]
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(PATH)
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnOKWhenGettingAllUsers() {
        given()
        .when()
            .get(PATH)
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
            .get(PATH + "/nonexistentuser")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
