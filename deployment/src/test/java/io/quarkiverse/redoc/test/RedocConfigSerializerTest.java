package io.quarkiverse.redoc.test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkiverse.redoc.deployment.RedocConfigSerializer;
import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

class RedocConfigSerializerTest {

    private static final String BASE_PATH = "serializer/";

    private final RedocConfigSerializer serializer = new RedocConfigSerializer();

    @ParameterizedTest
    @ValueSource(strings = { "unset", "all-true", "all-false" })
    void testSerialize(String testCase) throws IOException {
        RedocConfig config = loadConfig(BASE_PATH + testCase + "/config.properties");
        String expected = loadExpectedJson(BASE_PATH + testCase + "/expected.json");

        assertThatJson(serializer.serialize(config)).isEqualTo(expected);
    }

    private RedocConfig loadConfig(String propertiesFile) throws IOException {
        URL url = getClass().getClassLoader().getResource(propertiesFile);
        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withMapping(RedocConfig.class)
                .withSources(new PropertiesConfigSource(url))
                .build();

        return config.getConfigMapping(RedocConfig.class);
    }

    private String loadExpectedJson(String jsonFile) throws IOException {
        try (var is = getClass().getClassLoader().getResourceAsStream(jsonFile)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
