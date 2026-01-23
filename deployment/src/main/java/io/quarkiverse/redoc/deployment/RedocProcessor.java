package io.quarkiverse.redoc.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkiverse.redoc.deployment.model.DownloadUrlDto;
import io.quarkiverse.redoc.deployment.model.RedocConfigDto;
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

class RedocProcessor {

    private static final String FEATURE = "redoc";
    private static final String TEMPLATE_PATH = "templates/redoc.html";
    private final RedocConfigSerializer configSerializer = new RedocConfigSerializer();

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    RedocConfigBuildItem produceRedocConfig(
            RedocConfig config,
            SmallRyeOpenApiConfig openApiConfig,
            NonApplicationRootPathBuildItem nonApplicationRootPath) {

        String openApiBasePath = nonApplicationRootPath.resolvePath(openApiConfig.path());

        List<DownloadUrlDto> downloadUrls;
        if (config.downloadUrls().isEmpty()) {
            downloadUrls = List.of(
                    new DownloadUrlDto("JSON", openApiBasePath + ".json"),
                    new DownloadUrlDto("YAML", openApiBasePath + ".yaml"));
        } else {
            downloadUrls = config.downloadUrls().stream()
                    .map(du -> new DownloadUrlDto(du.title(), du.url()))
                    .toList();
        }

        return new RedocConfigBuildItem(new RedocConfigDto(
                config.path(),
                config.title(),
                config.alwaysInclude(),
                config.hideDownloadButtons().orElse(null),
                config.hideSchemaTitles().orElse(null),
                config.jsonSamplesExpandLevel().orElse(null),
                config.maxDisplayedEnumValues().orElse(null),
                config.layout().orElse(null),
                config.onlyRequiredInSamples().orElse(null),
                config.sortRequiredPropsFirst().orElse(null),
                config.schemasExpansionLevel().orElse(null),
                config.scrollYOffset().orElse(null),
                config.showExtensions().orElse(null),
                config.sanitize().orElse(null),
                downloadUrls,
                config.schemaDefinitionsTagName(),
                config.generatedSamplesMaxDepth().orElse(null),
                config.hidePropertiesPrefix().orElse(null)));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerRedocRoute(
            LaunchModeBuildItem launchMode,
            RedocConfigBuildItem configBuildItem,
            SmallRyeOpenApiConfig openApiConfig,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            RedocRecorder recorder,
            BuildProducer<RouteBuildItem> routeProducer) {

        RedocConfigDto config = configBuildItem.getConfig();

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

    private boolean shouldInclude(LaunchModeBuildItem launchMode, RedocConfigDto config) {
        return launchMode.getLaunchMode().isDevOrTest() || config.alwaysInclude();
    }

    private String generateRedocHtml(RedocConfigDto config, String openApiUrl) {
        String template = loadTemplate();
        String redocOptionsJson = configSerializer.serialize(config);

        return template
                .replace("{title}", escapeHtml(config.title()))
                .replace("{specUrl}", escapeJs(openApiUrl))
                .replace("{redocOptions}", redocOptionsJson)
                .replace("{redocJsPath}", "https://cdn.redoc.ly/redoc/v3.0.0-rc.0/redoc.standalone.js");
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
