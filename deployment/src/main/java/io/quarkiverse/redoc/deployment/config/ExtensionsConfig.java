package io.quarkiverse.redoc.deployment.config;

import java.util.Optional;

/**
 * Configuration for Redocly vendor extensions.
 */
public interface ExtensionsConfig {
    /**
     * Configuration for the x-logo OpenAPI vendor extension.
     * <p>
     * This extension adds a logo to the Redoc CE API documentation above the sidebar.
     * </p>
     */
    Optional<XLogoConfig> xLogo();
}
