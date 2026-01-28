package io.quarkiverse.redoc.deployment.model;

/**
 * DTO representing the x-logo OpenAPI vendor extension configuration.
 */
public record XLogoModel(
        String url,
        String backgroundColor,
        String altText,
        String href) {
}
