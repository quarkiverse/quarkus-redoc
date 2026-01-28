package io.quarkiverse.redoc.deployment.config;

/**
 * Configuration for Redocly vendor extensions.
 */
public interface RedocExtensionsConfig {
    /**
     * Configuration for the x-logo OpenAPI vendor extension.
     * <p>
     * This extension adds a logo to the Redoc API documentation above the sidebar.
     * If `quarkus.redoc.extensions.x-logo.url` is not configured, the extension will automatically
     * look for a file starting with 'redoc-logo.' in the classpath under META-INF/resources/ and add it to the
     * OpenAPI spec if found.
     * </p>
     */
    XLogoConfig xLogo();
}
