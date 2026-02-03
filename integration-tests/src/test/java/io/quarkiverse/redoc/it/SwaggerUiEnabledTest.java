package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(SwaggerUiEnabledTest.class)
public class SwaggerUiEnabledTest implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("quarkus.swagger-ui.enabled", "true");
    }

    @Test
    public void swaggerUiShouldBeDisabled() {
        given()
                .when().get("/q/swagger-ui")
                .then()
                .statusCode(200);
    }

    @Test
    public void redocShouldBeAvailable() {
        given()
                .when().get("/q/redoc")
                .then()
                .statusCode(200);
    }
}
