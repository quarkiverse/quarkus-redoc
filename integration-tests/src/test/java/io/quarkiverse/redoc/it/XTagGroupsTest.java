package io.quarkiverse.redoc.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class XTagGroupsTest {

    @Test
    public void testXTagGroupsExtensionInOpenAPI() {
        // Verify that the x-tagGroups extension is added to the root OpenAPI document
        given()
                .when().get("/q/openapi.json")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("'x-tagGroups'", notNullValue())
                .body("'x-tagGroups'", hasSize(3))
                .body("'x-tagGroups'[0].name", equalTo("User Management"))
                .body("'x-tagGroups'[0].tags", hasItems("users", "authentication"))
                .body("'x-tagGroups'[1].name", equalTo("Content"))
                .body("'x-tagGroups'[1].tags", hasItems("posts", "comments"));
    }

    @Test
    public void testCatchAllGroupContainsUnassignedTags() {
        // Verify that the catch-all "Other" group contains the "misc" tag
        // which is defined in RedocResource but not assigned to any explicit group
        given()
                .when().get("/q/openapi.json")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("'x-tagGroups'[2].name", equalTo("Other"))
                .body("'x-tagGroups'[2].tags", hasItems("misc"));
    }
}
