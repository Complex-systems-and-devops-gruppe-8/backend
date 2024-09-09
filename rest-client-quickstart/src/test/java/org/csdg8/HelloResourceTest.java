package org.csdg8;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.net.URL;

@QuarkusTest
public class HelloResourceTest {

    @TestHTTPEndpoint(HelloResource.class)  
    @TestHTTPResource
    URL url;
    
    @Test
    public void testHelloEndpoint() {
        given()
            .when().get(url)
            .then()
                .statusCode(200)
                .body(is("Hello, World!"));
    }
}
