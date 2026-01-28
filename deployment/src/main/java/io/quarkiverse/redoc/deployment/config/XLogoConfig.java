package io.quarkiverse.redoc.deployment.config;

import java.util.Optional;

/**
 * Configuration for the x-logo OpenAPI vendor extension.
 * <p>
 * This extension adds a logo to the Redoc API documentation above the sidebar.
 * </p>
 */
public interface XLogoConfig {
    /**
     * The URL to the logo image. This can be:
     * <ul>
     * <li>An absolute URL (e.g., https://example.com/logo.png)</li>
     * <li>A relative path that will be served by the application</li>
     * <li>If not set, the extension will look for a file starting with 'redoc-logo.' in the classpath under
     * META-INF/resources/</li>
     * </ul>
     */
    Optional<String> url();

    /**
     * The background color for the logo area (in hexadecimal format, e.g., #FFFFFF).
     */
    Optional<String> backgroundColor();

    /**
     * Alternate text for the logo image, shown if the image cannot be loaded.
     */
    Optional<String> altText();

    /**
     * The URL the logo links to when clicked.
     */
    Optional<String> href();
}
