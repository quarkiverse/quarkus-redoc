package io.quarkiverse.redoc.deployment;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.quarkiverse.redoc.deployment.config.DownloadUrlConfig;
import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Serializes RedocConfig to JSON for Redoc CE.
 */
public class RedocConfigSerializer {

    public String serialize(RedocConfig config) {
        JsonObject json = new JsonObject();

        config.hideDownloadButtons().ifPresent(v -> json.put("hideDownloadButtons", v));
        config.hideSchemaTitles().ifPresent(v -> json.put("hideSchemaTitles", v));
        config.jsonSamplesExpandLevel().ifPresent(v -> json.put("jsonSamplesExpandLevel", parseIntOrString(v)));
        config.maxDisplayedEnumValues().ifPresent(v -> json.put("maxDisplayedEnumValues", v));
        config.layout().ifPresent(v -> json.put("layout", v.getValue()));
        config.onlyRequiredInSamples().ifPresent(v -> json.put("onlyRequiredInSamples", v));
        config.sortRequiredPropsFirst().ifPresent(v -> json.put("sortRequiredPropsFirst", v));
        config.schemasExpansionLevel().ifPresent(v -> json.put("schemasExpansionLevel", parseIntOrString(v)));
        config.scrollYOffset().ifPresent(v -> json.put("scrollYOffset", v));
        config.showExtensions().ifPresent(v -> json.put("showExtensions", parseShowExtensions(v)));
        config.sanitize().ifPresent(v -> json.put("sanitize", v));

        if (!config.downloadUrls().isEmpty()) {
            JsonObject downloadUrls = new JsonObject();
            for (DownloadUrlConfig du : config.downloadUrls()) {
                downloadUrls.put(du.title(), new JsonObject().put("url", du.url()));
            }
            json.put("downloadUrls", downloadUrls);
        }

        json.put("schemaDefinitionsTagName", config.schemaDefinitionsTagName());

        config.generatedSamplesMaxDepth().ifPresent(v -> json.put("generatedSamplesMaxDepth", v));
        config.hidePropertiesPrefix().ifPresent(v -> json.put("hidePropertiesPrefix", v));

        return json.encode();
    }

    private Object parseIntOrString(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private Object parseShowExtensions(String value) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            return new JsonArray(Arrays.stream(value.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList()));
        }
    }
}
