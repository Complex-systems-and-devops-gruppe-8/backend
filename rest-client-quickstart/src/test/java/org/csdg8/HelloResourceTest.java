package org.csdg8;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.net.URL;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class HelloResourceTest {

    @TestHTTPEndpoint(HelloResource.class)  
    @TestHTTPResource
    URL url;
    
    @Test
    public void testHelloEndpoint() {
        given()
            .when().get(url + "/all")
            .then()
                .statusCode(200)
                .body(is("Hello, World!"));
    }
}
