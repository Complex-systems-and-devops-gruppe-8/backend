package org.csdg8.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.csdg8.user.dto.CreateUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.siren4j.Siren4J;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import jakarta.transaction.Transactional;

@QuarkusTest
public class UserResourceTest {

    @SuppressWarnings("unused")
    private Long adminId;
    private Long userId;

    @TestHTTPEndpoint(UserResource.class)
    @TestHTTPResource
    URL url;

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

    @BeforeEach
    @Transactional
    public void setup() {
        this.adminId = User.add("admin", "admin1234", Set.of("admin"));
        this.userId = User.add("user", "user1234", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldReturnCreatedWhenRegisteringValidUser() {
        CreateUserRequest request = new CreateUserRequest("john", "password123");

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
        CreateUserRequest request = new CreateUserRequest("admin", "password123");

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
        CreateUserRequest request = new CreateUserRequest("u", "short");

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
            .contentType(Siren4J.JSON_MEDIATYPE)
            .body(not(empty()));
    }

    @Test
    public void shouldReturnOKWhenGettingExistingUser() {
        given()
        .when()
            .get(url + "/" + this.userId)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(Siren4J.JSON_MEDIATYPE)
            .body(not(empty()));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenGettingNonExistentUser() {
        given()
        .when()
            .get(url + "/99999")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
