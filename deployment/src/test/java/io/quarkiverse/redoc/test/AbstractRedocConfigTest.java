package io.quarkiverse.redoc.test;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public abstract class AbstractRedocConfigTest {

    private static final Pattern REDOC_OPTIONS_PATTERN = Pattern.compile(
            "const redocOptions = (\\{.*?});",
            Pattern.DOTALL);

    protected abstract String getConfigPath();

    @Test
    void testRedocConfig() throws IOException {
        String html = given()
                .when().get("/q/redoc")
                .then().statusCode(200)
                .extract().body().asString();

        String redocOptionsJson = extractRedocOptions(html);
        String expected = loadResource(getConfigPath() + "expected.json");

        assertThatJson(redocOptionsJson).isEqualTo(expected);
    }

    private String extractRedocOptions(String html) {
        Matcher matcher = REDOC_OPTIONS_PATTERN.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new AssertionError("Could not extract redocOptions from HTML: " + html);
    }

    private String loadResource(String path) throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
