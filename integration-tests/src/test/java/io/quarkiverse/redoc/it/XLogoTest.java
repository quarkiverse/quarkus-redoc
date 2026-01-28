package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
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
                .body("info.'x-logo'.url", equalTo("/redoc-logo.png"))
                .body("info.'x-logo'.backgroundColor", equalTo("#FFFFFF"))
                .body("info.'x-logo'.altText", equalTo("Test Logo"))
                .body("info.'x-logo'.href", equalTo("https://example.com"));
    }
}
