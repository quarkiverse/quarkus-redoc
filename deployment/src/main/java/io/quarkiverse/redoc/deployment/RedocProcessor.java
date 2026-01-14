package io.quarkiverse.redoc.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.quarkiverse.redoc.deployment.config.DownloadUrlConfig;
import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkiverse.redoc.runtime.RedocRecorder;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.smallrye.openapi.common.deployment.SmallRyeOpenApiConfig;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

class RedocProcessor {

    private static final String FEATURE = "redoc";
    private static final String TEMPLATE_PATH = "templates/redoc.html";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerRedocRoute(
            LaunchModeBuildItem launchMode,
            RedocConfig config,
            SmallRyeOpenApiConfig openApiConfig,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            RedocRecorder recorder,
            BuildProducer<RouteBuildItem> routeProducer) {

        if (!shouldInclude(launchMode, config)) {
            return;
        }

        String openApiUrl = nonApplicationRootPath.resolvePath(openApiConfig.path());
        String generatedHtml = generateRedocHtml(config, openApiUrl);

        routeProducer.produce(
                nonApplicationRootPath.routeBuilder()
                        .route(config.path())
                        .displayOnNotFoundPage("Redoc CE")
                        .handler(recorder.createHandler(generatedHtml))
                        .build());
    }

    private boolean shouldInclude(LaunchModeBuildItem launchMode, RedocConfig config) {
        return launchMode.getLaunchMode().isDevOrTest() || config.alwaysInclude();
    }

    private String generateRedocHtml(RedocConfig config, String openApiUrl) {
        String template = loadTemplate();
        String redocOptionsJson = serializeConfigToJson(config);

        return template
                .replace("{title}", escapeHtml(config.title()))
                .replace("{specUrl}", escapeJs(openApiUrl))
                .replace("{redocOptions}", redocOptionsJson)
                .replace("{redocJsPath}", "https://cdn.redoc.ly/redoc/v3.0.0-rc.0/redoc.standalone.js");
    }

    private String serializeConfigToJson(RedocConfig config) {
        JsonObject json = new JsonObject();

        // Only serialize values that are explicitly set (Optionals present)
        // This allows Redocly to use its own defaults when not configured
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

        // schemaDefinitionsTagName has @WithDefault("Schemas"), only include if different
        if (!"Schemas".equals(config.schemaDefinitionsTagName())) {
            json.put("schemaDefinitionsTagName", config.schemaDefinitionsTagName());
        }

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

    private String loadTemplate() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                throw new RuntimeException("Could not find Redoc template at " + TEMPLATE_PATH);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Redoc template", e);
        }
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String escapeJs(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
