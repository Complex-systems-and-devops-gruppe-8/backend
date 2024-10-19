package org.csdg8.util;

import static org.hamcrest.Matchers.equalTo;

import com.google.code.siren4j.component.impl.ActionImpl.Method;

import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;

/**
 * Utility class to assert properties of Siren JSON RestAssured {@link Response} bodies.
 */
public class SirenAssertion {
    /**
     * Parses the given {@code response} JSON, finds the action by
     * {@code actionName}
     * and asserts it contains a method field equal to the supplied
     * {@code methodType}.
     * 
     * @param response
     * @param actionName
     * @param methodType
     */
    public static void itShouldHaveMethod(Response response, String actionName, Method methodType) {
        response.then()
                .body("actions.find { it.name == '" + actionName + "' }.method",
                        equalTo(methodType.toString()));
    }

    /**
     * Parses the given {@code response} JSON, finds the action by
     * {@code actionName}
     * and asserts it contains a type field equal to the {@code application/json}.
     * 
     * @param response
     * @param actionName
     */
    public static void itShouldHaveJSONAcceptType(Response response, String actionName) {
        response.then()
                .body("actions.find { it.name == '" + actionName + "' }.type",
                        equalTo(MediaType.APPLICATION_JSON.toString()));
    }

    /**
     * Parses the given {@code response} JSON, finds the action by
     * {@code actionName}
     * and asserts it contains the supplied {@code href}.
     * 
     * @param response   the response body to parse
     * @param actionName the name of the action
     * @param href       the expected href uri
     */
    public static void itShouldHaveHref(Response response, String actionName, String href) {
        response.then()
                .body("actions.find { it.name == '" + actionName + "' }.href",
                        equalTo(href));
    }

    /**
     * Parses the given {@code response} JSON, finds the action by
     * {@code actionName}
     * and asserts it has the supplied {@code fieldName}, {@code type},
     * and whether the field {@code isRequired}.
     * 
     * @param response   the response body to parse
     * @param actionName the name of the action
     * @param fieldName  the name of the expected field in the action
     * @param type       the type of the field in the expected field
     * @param isRequired whether the field is expected to be required.
     */
    public static void itShouldHaveField(Response response, String actionName, String fieldName, String type,
            boolean isRequired) {
        response.then()
                .body("actions.find { it.name == '" + actionName + "' }.fields.find { it.name == '" + fieldName
                        + "' }.required",
                        equalTo(isRequired))
                .body("actions.find { it.name == '" + actionName + "' }.fields.find { it.name == '" + fieldName
                        + "' }.type",
                        equalTo(type));
    }
}