package io.quarkiverse.redoc.deployment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkiverse.redoc.deployment.config.XLogoConfig;
import io.quarkiverse.redoc.runtime.RedocRecorder;
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

    @BuildStep
    void addXLogoToOpenAPI(
            RedocConfig config,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            CurateOutcomeBuildItem curateOutcome,
            BuildProducer<AddToOpenAPIDefinitionBuildItem> openApiProducer) {

        XLogoConfig xLogoConfig = config.extensions().xLogo();

        // Determine the logo URL
        String logoUrl = determineLogoUrl(xLogoConfig, config.path(), nonApplicationRootPath, curateOutcome);

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
            CurateOutcomeBuildItem curateOutcome,
            RedocRecorder recorder,
            BuildProducer<RouteBuildItem> routeProducer) {

        // Only register the route if we're serving a logo from the classpath
        XLogoConfig xLogoConfig = config.extensions().xLogo();
        if (xLogoConfig.url().isPresent()) {
            // User configured an external URL, no need to serve the logo
            return;
        }

        // Check if logo file exists in classpath
        LogoInfo logoInfo = findLogoInClasspath(curateOutcome);
        if (logoInfo == null) {
            return;
        }

        // Register a route to serve the logo
        String logoPath = config.path() + "/redoc-logo" + logoInfo.extension();
        routeProducer.produce(
                nonApplicationRootPath.routeBuilder()
                        .route(logoPath)
                        .handler(recorder.createLogoHandler(logoInfo.resourcePath()))
                        .build());
    }

    private String determineLogoUrl(XLogoConfig xLogoConfig, String configPath,
            NonApplicationRootPathBuildItem nonApplicationRootPath,
            CurateOutcomeBuildItem curateOutcome) {
        // If a URL is explicitly configured, use it
        if (xLogoConfig.url().isPresent()) {
            return xLogoConfig.url().get();
        }

        // Check if redoc-logo file exists in the classpath
        LogoInfo logoInfo = findLogoInClasspath(curateOutcome);
        if (logoInfo != null) {
            // Return the path where we'll serve the logo
            return nonApplicationRootPath.resolvePath(configPath + "/redoc-logo" + logoInfo.extension());
        }

        return null;
    }

    private LogoInfo findLogoInClasspath(CurateOutcomeBuildItem curateOutcome) {
        ResolvedDependency appArtifact = curateOutcome.getApplicationModel().getAppArtifact();
        if (appArtifact != null && appArtifact.getResolvedPaths() != null) {
            for (var basePath : appArtifact.getResolvedPaths()) {
                // Look directly in the resources directory, not META-INF/resources
                // since we're serving the logo ourselves with a custom route
                Path resourcesPath = basePath;

                // Check if this is a directory (during development/testing)
                if (Files.exists(resourcesPath) && Files.isDirectory(resourcesPath)) {
                    try (Stream<Path> files = Files.list(resourcesPath)) {
                        var logoFile = files
                                .filter(Files::isRegularFile)
                                .filter(p -> {
                                    String name = p.getFileName().toString();
                                    // Check that it starts with "redoc-logo." and has a valid extension
                                    if (!name.startsWith("redoc-logo.")) {
                                        return false;
                                    }
                                    int dotIndex = name.lastIndexOf('.');
                                    // Ensure there's at least one character after the dot (the extension)
                                    return dotIndex >= "redoc-logo.".length() - 1 && dotIndex < name.length() - 1;
                                })
                                .findFirst();

                        if (logoFile.isPresent()) {
                            String fileName = logoFile.get().getFileName().toString();
                            String extension = fileName.substring(fileName.lastIndexOf('.'));
                            String resourcePath = fileName;
                            return new LogoInfo(resourcePath, extension);
                        }
                    } catch (IOException e) {
                        // Continue if directory listing fails
                    }
                }
            }
        }
        return null;
    }

    private record LogoInfo(String resourcePath, String extension) {
    }
}
