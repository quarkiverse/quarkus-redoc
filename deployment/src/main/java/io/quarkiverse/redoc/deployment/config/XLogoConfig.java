package io.quarkiverse.redoc.deployment.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigDocDefault;

/**
 * Configuration for the x-logo OpenAPI vendor extension.
 * <p>
 * This extension adds a logo to the Redoc CE API documentation above the sidebar.
 * </p>
 */
public interface XLogoConfig {
    /**
     * The URL to the logo image. This can be:
     * <ul>
     * <li>An absolute URL (e.g., https://example.com/logo.png). It SHOULD be an absolute URL so your API definition is usable
     * from any location.</li>
     * <li>A relative path that will have to be served by the application</li>
     * </ul>
     */
    String url();

    /**
     * Optional background color for the logo area in hexadecimal format, e.g., #FFFFFF.
     */
    Optional<String> backgroundColor();

    /**
     * Optional text to use for the alt HTML tag on the logo image.
     */
    @ConfigDocDefault("logo")
    Optional<String> altText();

    /**
     * Optional URL pointing to the contact page.
     */
    @ConfigDocDefault("Defaults to 'info.contact.url' field from the OpenAPI definition.")
    Optional<String> href();
}
