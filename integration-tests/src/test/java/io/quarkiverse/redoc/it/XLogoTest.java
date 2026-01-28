package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

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
                .body("info.'x-logo'", notNullValue())
                .body("info.'x-logo'.url", notNullValue());
    }

    @Test
    public void testLogoResourceIsAccessible() {
        // Verify that the redoc-logo.png file is accessible via the custom route
        given()
                .when().get("/q/redoc/redoc-logo.png")
                .then()
                .statusCode(200)
                .contentType("image/png");
    }
}
