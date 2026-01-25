package io.quarkiverse.redoc.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.quarkiverse.redoc.deployment.model.DownloadUrlModel;
import io.quarkiverse.redoc.deployment.model.RedocConfigModel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Serializes RedocConfigDto to JSON for Redoc CE.
 */
public class RedocConfigSerializer {

    public String serialize(RedocConfigModel config) {
        JsonObject json = new JsonObject();

        if (config.routingBasePath() != null) {
            json.put("routingBasePath", config.routingBasePath());
        }
        if (config.hideDownloadButtons() != null) {
            json.put("hideDownloadButtons", config.hideDownloadButtons());
        }
        if (config.hideSchemaTitles() != null) {
            json.put("hideSchemaTitles", config.hideSchemaTitles());
        }
        if (config.jsonSamplesExpandLevel() != null) {
            json.put("jsonSamplesExpandLevel", parseIntOrString(config.jsonSamplesExpandLevel()));
        }
        if (config.maxDisplayedEnumValues() != null) {
            json.put("maxDisplayedEnumValues", config.maxDisplayedEnumValues());
        }
        if (config.layout() != null) {
            json.put("layout", config.layout().getValue());
        }
        if (config.onlyRequiredInSamples() != null) {
            json.put("onlyRequiredInSamples", config.onlyRequiredInSamples());
        }
        if (config.sortRequiredPropsFirst() != null) {
            json.put("sortRequiredPropsFirst", config.sortRequiredPropsFirst());
        }
        if (config.schemasExpansionLevel() != null) {
            json.put("schemasExpansionLevel", parseIntOrString(config.schemasExpansionLevel()));
        }
        if (config.scrollYOffset() != null) {
            json.put("scrollYOffset", config.scrollYOffset());
        }
        if (config.showExtensions() != null) {
            json.put("showExtensions", parseShowExtensions(config.showExtensions()));
        }
        if (config.sanitize() != null) {
            json.put("sanitize", config.sanitize());
        }

        JsonArray downloadUrls = new JsonArray();
        for (DownloadUrlModel du : config.downloadUrlModels()) {
            downloadUrls.add(new JsonObject().put("title", du.title()).put("url", du.url()));
        }
        json.put("downloadUrls", downloadUrls);

        json.put("schemaDefinitionsTagName", config.schemaDefinitionsTagName());

        if (config.generatedSamplesMaxDepth() != null) {
            json.put("generatedSamplesMaxDepth", config.generatedSamplesMaxDepth());
        }
        if (config.hidePropertiesPrefix() != null) {
            json.put("hidePropertiesPrefix", config.hidePropertiesPrefix());
        }
        if (!config.ignoreNamedSchemas().isEmpty()) {
            json.put("ignoreNamedSchemas", new JsonArray(new ArrayList<>(config.ignoreNamedSchemas())));
        }
        if (config.hideLoading() != null) {
            json.put("hideLoading", config.hideLoading());
        }
        if (config.hideSidebar() != null) {
            json.put("hideSidebar", config.hideSidebar());
        }

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
