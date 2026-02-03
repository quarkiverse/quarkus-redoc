package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SwaggerUiDisabledTest {

    @Test
    public void swaggerUiShouldBeDisabled() {
        given()
                .when().get("/q/swagger-ui")
                .then()
                .statusCode(404);
    }

    @Test
    public void redocShouldBeAvailable() {
        given()
                .when().get("/q/redoc")
                .then()
                .statusCode(200);
    }
}
