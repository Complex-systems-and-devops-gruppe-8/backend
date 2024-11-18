package org.csdg8.game;

import static io.restassured.RestAssured.given;
import static org.csdg8.util.SirenAssertion.responseShouldHaveLink;

import java.net.URL;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class GameResourceTest {

    @TestHTTPResource
    @TestHTTPEndpoint(GameResource.class)
    URL gameUrl;

    String relativeGameUrl;

    @BeforeAll
    public void setup() {
        relativeGameUrl = gameUrl.getPath();
    }

    @Test
    public void shouldBeReachableViaHypermedia() {
        Response response = given().when()
                .get("/")
                .then()
                .extract()
                .response();

        responseShouldHaveLink(response, "game", relativeGameUrl);
    }

    @Test
    @TestSecurity() // blank security
    public void shouldReturnUnauthorized_whenNotAUser() {
        given()
                .when()
                .get(gameUrl)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @TestSecurity(user = "user") // no role
    public void shouldReturnForbidden_whenMissingUserRole() {
        given()
                .when()
                .get(gameUrl)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @TestSecurity(user = "user", roles = "user")
    public void whenIGetTheGameResponse() {
        Response response = given()
                .when()
                .get(gameUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        itShouldHaveLinkToCoinFlip(response);
    }

    private void itShouldHaveLinkToCoinFlip(Response response) {
        String linkRel = "coin-flip";
        String linkHref = relativeGameUrl + "/coin-flip";
        responseShouldHaveLink(response, linkRel, linkHref);
    }
}
