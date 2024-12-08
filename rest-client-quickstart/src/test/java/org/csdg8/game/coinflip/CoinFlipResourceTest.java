package org.csdg8.game.coinflip;

import java.net.URL;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.csdg8.user.User;
import static org.csdg8.util.SirenAssertion.actionFieldShouldHaveOption;
import static org.csdg8.util.SirenAssertion.actionShouldHaveField;
import static org.csdg8.util.SirenAssertion.actionShouldHaveHref;
import static org.csdg8.util.SirenAssertion.actionShouldHaveMethod;
import static org.csdg8.util.SirenAssertion.actionShouldHaveType;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.code.siren4j.component.impl.ActionImpl.Method;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestSecurity(user = "user", roles = "user")
public class CoinFlipResourceTest {

    @TestHTTPResource
    @TestHTTPEndpoint(CoinFlipResource.class)
    URL coinFlipUrl;

    @Inject
    CoinFlipResource coinFlipResource;


    @BeforeEach
    @Transactional
    public void setup() {
        User.deleteAll();
        User.add("user", "user1234", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldBeReachableViaHypermedia() {
        String coinFlipHrefPath = given()
            .when()
            .get("/")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("links.find { it.rel.contains('game') }.href")
            .toString();

        given()
            .when()
            .get(coinFlipHrefPath)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("links.find { it.rel.contains('coin-flip') }.href", equalTo("/game/coin-flip"));
    }

    @Test
    @TestSecurity() //blank security
    public void shouldReturnUnauthorized_whenNotAUser() {
        given()
            .when()
            .get("/game/coin-flip")
            .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @TestSecurity(user = "user") //no role
    public void shouldReturnForbidden_whenMissingUserRole() {
        given()
            .when()
            .get("/game/coin-flip")
            .then()
            .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @TestSecurity(user = "user", roles = "user")
    public void whenIGetTheGameResponse() {
        Response response = given()
                .when()
                .get(coinFlipUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        itShouldHaveActionToPlay(response);
        itShouldHaveActionToGetGame(response);
    }

    private void itShouldHaveActionToPlay(Response response) {
        String actionName = "play";
        actionShouldHaveMethod(response, actionName, Method.POST);
        actionShouldHaveType(response, actionName, MediaType.APPLICATION_JSON);
        actionShouldHaveHref(response, actionName, "/game/coin-flip");

        String choiceField = "choice";
        actionShouldHaveField(response, actionName, choiceField, "TEXT", true);
        actionFieldShouldHaveOption(response, actionName, choiceField, "HEADS", false);
        actionFieldShouldHaveOption(response, actionName, choiceField, "TAILS", false);

        String betAmountField = "betAmount";
        actionShouldHaveField(response, actionName, betAmountField, "NUMBER", true);
    }

    private void itShouldHaveActionToGetGame(Response response) {
        String actionName = "result";
        actionShouldHaveMethod(response, actionName, Method.GET);
        actionShouldHaveType(response, actionName, MediaType.APPLICATION_FORM_URLENCODED);
        actionShouldHaveHref(response, actionName, "/game/coin-flip/{id}");
    }

    /* TODO figure out a way to write a test for POST /game/coin-flip
         and subsequently GET /game/coin-flip/<id>. Current issue is a game is saved
         to the user's "sub" (Subject) ID from their token. It is not possible to
         know the user ID at compile time making the jwtSecurity annotation claims
         unfeasable.
    */
}
