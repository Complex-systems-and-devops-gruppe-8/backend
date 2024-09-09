package org.csdg8.security.jpa;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.csdg8.HelloResource;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JpaSecurityRealmTest {

    @TestHTTPEndpoint(HelloResource.class)
    @TestHTTPResource
    URL url;

    @Test
    void shouldAccessPublicWhenAnonymous() {
        get(url + "/all")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    void shouldNotAccessAdminWhenAnonymous() {
        get(url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    void shouldAccessAdminWhenAdminAuthenticated() {
        given().auth().preemptive().basic("admin", "admin")
                .when()
                .get(url + "/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given().auth().preemptive().basic("admin", "admin")
                .when()
                .get(url + "/user")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        given().auth().preemptive().basic("user", "user")
                .when()
                .get(url + "/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello, user!"));
    }
}
