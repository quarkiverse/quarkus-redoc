package io.quarkiverse.redoc.deployment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkiverse.redoc.deployment.config.XLogoConfig;
import io.quarkiverse.redoc.runtime.RedocRecorder;
import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.maven.dependency.ResolvedDependency;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;

/**
 * Processor for adding Redocly vendor extensions to the OpenAPI document.
 */
class RedocVendorExtensionProcessor {

    private static final String REDOC_LOGO_FILE_PREFIX = "redoc-logo.";

    @BuildStep
    void addXLogoToOpenAPI(
            RedocConfig config,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            BuildProducer<AddToOpenAPIDefinitionBuildItem> openApiProducer, Optional<RedocLogoBuildItem> redocLogoBuildItem) {

        XLogoConfig xLogoConfig = config.extensions().xLogo();

        // Determine the logo URL
        String logoUrl = determineLogoUrl(xLogoConfig, config.path(), nonApplicationRootPath, redocLogoBuildItem);

        // If no logo URL is determined, skip adding the extension
        if (logoUrl == null || logoUrl.isEmpty()) {
            return;
        }

        // Create and register the OASFilter
        XLogoOASFilter filter = new XLogoOASFilter(
                logoUrl,
                xLogoConfig.backgroundColor().orElse(null),
                xLogoConfig.altText().orElse(null),
                xLogoConfig.href().orElse(null));

        openApiProducer.produce(new AddToOpenAPIDefinitionBuildItem(filter));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerLogoRoute(
            RedocConfig config,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            RedocRecorder recorder,
            BuildProducer<RouteBuildItem> routeProducer, Optional<RedocLogoBuildItem> redocLogoBuildItem) {

        // Only register the route if we're serving a logo from the classpath
        XLogoConfig xLogoConfig = config.extensions().xLogo();
        if (xLogoConfig.url().isPresent()) {
            // User configured an external URL, no need to serve the logo
            return;
        }
        // Check if logo file exists in classpath
        if (!redocLogoBuildItem.isPresent()) {
            return;
        }

        // Register a route to serve the logo
        String logoPath = config.path() + "/" + REDOC_LOGO_FILE_PREFIX + redocLogoBuildItem.get().extension;
        routeProducer.produce(
                nonApplicationRootPath.routeBuilder()
                        .route(logoPath)
                        .handler(recorder.createLogoHandler(redocLogoBuildItem.get().fileName))
                        .build());
    }

    private String determineLogoUrl(XLogoConfig xLogoConfig, String configPath,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            Optional<RedocLogoBuildItem> redocLogoBuildItem) {

        // configured logo takes precedence over file

        if (xLogoConfig.url().isPresent()) {
            return xLogoConfig.url().get();
        }

        return redocLogoBuildItem.map(logoBuildItem -> nonApplicationRootPath
                .resolvePath(configPath + "/" + REDOC_LOGO_FILE_PREFIX + logoBuildItem.extension)).orElse(null);

    }

    @BuildStep
    RedocLogoBuildItem detectCustomRedocLogo(CurateOutcomeBuildItem curateOutcome) {
        ResolvedDependency appArtifact = curateOutcome.getApplicationModel().getAppArtifact();
        if (appArtifact != null && appArtifact.getResolvedPaths() != null) {
            for (var basePath : appArtifact.getResolvedPaths()) {
                if (Files.exists(basePath) && Files.isDirectory(basePath)) {
                    try (Stream<Path> files = Files.list(basePath)) {
                        var logoFile = files
                                .filter(Files::isRegularFile)
                                .filter(p -> {
                                    String name = p.getFileName().toString();
                                    if (!name.startsWith(REDOC_LOGO_FILE_PREFIX)) {
                                        return false;
                                    }
                                    int dotIndex = name.lastIndexOf('.');
                                    // Ensure there's at least one character after the dot (the extension)
                                    return dotIndex >= REDOC_LOGO_FILE_PREFIX.length() - 1 && dotIndex < name.length() - 1;
                                })
                                .findFirst();

                        if (logoFile.isPresent()) {
                            String fileName = logoFile.get().getFileName().toString();
                            return new RedocLogoBuildItem(fileName, fileName.substring(REDOC_LOGO_FILE_PREFIX.length()));
                        }
                    } catch (IOException e) {
                        // Continue if directory listing fails
                    }
                }
            }
        }
        return null;
    }

    private final class RedocLogoBuildItem extends SimpleBuildItem {
        String fileName;
        String extension;

        public RedocLogoBuildItem(String fileName, String extension) {
            this.fileName = fileName;
            this.extension = extension;
        }
    }
}
