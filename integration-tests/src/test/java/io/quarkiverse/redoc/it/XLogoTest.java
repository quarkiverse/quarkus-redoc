package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class XLogoTest {

    @Test
    public void testXLogoExtensionInOpenAPI() {
        // Verify that the x-logo extension is added to the OpenAPI document
        given()
                .when().get("/q/openapi.json")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(containsString("\"x-logo\""));
    }

    @Test
    public void testLogoResourceIsAccessible() {
        // Verify that the logo.png file is accessible
        given()
                .when().get("/logo.png")
                .then()
                .statusCode(200)
                .contentType("image/png");
    }
}
