package io.quarkiverse.redoc.deployment.config;

/**
 * Configuration for a custom download URL.
 */
public interface DownloadUrlConfig {
    /**
     * The title/label for this download option (e.g., "YAML", "JSON").
     */
    String title();

    /**
     * The URL to download the API definition from.
     */
    String url();
}
