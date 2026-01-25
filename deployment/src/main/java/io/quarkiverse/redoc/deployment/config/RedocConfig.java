package io.quarkiverse.redoc.deployment.config;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.quarkus.runtime.annotations.ConfigDocDefault;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.redoc")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface RedocConfig {

    String DEFAULT_SCHEMA_DEFINITIONS_TAG_NAME = "Schemas";

    /**
     * The path where Redoc UI is served (relative to non-application root path).
     * The default is "redoc", which means Redoc will be available at /q/redoc.
     */
    @WithDefault("redoc")
    String path();

    /**
     * The base path of Redocly, combined with any other preceding path. Redocly performs URL manipulation when navigating to
     * OpenAPI tags using the sidebar.
     * <p/>
     * Useful if this Quarkus application is served behind a reverse proxy which performs URL rewriting.
     * <p/>
     * E.g. If the reverse proxy rewrites / to /my-application, and the path config property is `redoc`, then this
     * routing-base-path property should be set to `/my-application/q/redoc`.
     */
    @ConfigDocDefault("Defaults to the resolved path (e.g., /q/redoc)")
    Optional<String> routingBasePath();

    /**
     * The title displayed in the browser tab.
     */
    @WithDefault("API Documentation")
    String title();

    /**
     * If true, Redoc is included in production builds.
     * By default, Redoc is only available in dev and test modes.
     */
    @WithDefault("false")
    boolean alwaysInclude();

    // --- Redocly 3.0 Configuration ---
    // Options marked as "Supported in Redoc CE" from https://redocly.com/docs/realm/config/openapi

    /**
     * Hides the 'Download' button for saving the API definition source file. This setting does not make the API definition
     * private; it just hides the button.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> hideDownloadButtons();

    /**
     * Hides the schema title next to the type.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> hideSchemaTitles();

    /**
     * Sets the default expand level for JSON payload samples (response and request body). The default value is 2, and the
     * maximum supported value is '+Infinity'. It can also be configured as a string with the special value all that expands all
     * levels.
     */
    @ConfigDocDefault("2")
    Optional<String> jsonSamplesExpandLevel();

    /**
     * Displays only the specified number of enum values. The remaining values are hidden in an expandable area. By default 10
     * values are displayed which is ideal for usability.
     */
    @ConfigDocDefault("10")
    Optional<Integer> maxDisplayedEnumValues();

    /**
     * Controls how API documentation panels are displayed on the page.
     * <ul>
     * <li>{@code three-panel} - Standard layout with sidebar on the left, main content in the center, and code samples on the
     * right</li>
     * <li>{@code stacked} - Alternative layout that integrates the right panel into the middle panel, creating a single-column
     * view. Better for narrower viewports or sequential reading</li>
     * </ul>
     */
    // TODO: Figure out if available in Redoc CE, I sent them an e-mail
    @ConfigDocDefault("three-panel")
    Optional<Layout> layout();

    /**
     * Shows only required fields in request samples. Use this option if you have a large number of optional fields that can
     * make request samples difficult to read.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> onlyRequiredInSamples();

    /**
     * Shows required properties in schemas first, ordered in the same order as in the required array.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> sortRequiredPropsFirst();

    /**
     * Specifies whether to automatically expand schemas in Reference docs. Set it to all to expand all schemas regardless of
     * their level, or set it to a number to expand schemas up to the specified level. For example, schemasExpansionLevel: 3
     * expands schemas up to three levels deep.
     */
    @ConfigDocDefault("4")
    Optional<String> schemasExpansionLevel();

    /**
     * Specifies a vertical scroll-offset in pixels. This setting is useful when there are fixed positioned elements at the
     * top of the page, such as navbars, headers, etc.
     */
    Optional<Integer> scrollYOffset();

    /**
     * Shows specification extensions ('x-' fields). Extensions used by Redoc are ignored.
     * <p/>
     * The value can be:
     * <ul>
     * <li>{@code true} - show all specification extensions</li>
     * <li>{@code false} - hide all specification extensions (default)</li>
     * <li>a comma-separated list of extension names to display (e.g., {@code x-logo,x-code-samples})</li>
     * </ul>
     */
    @ConfigDocDefault("false")
    Optional<String> showExtensions();

    /**
     * If set to true, the spec is considered untrusted and all HTML/Markdown is sanitized.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> sanitize();

    /**
     * Custom download URLs for the API definition.
     */
    @ConfigDocDefault("Defaults to showing download links for yaml and json")
    List<DownloadUrlConfig> downloadUrls();

    /**
     * If a value is set, all of the schemas are rendered with the designated tag name. The schemas then render and display in
     * the sidebar navigation (with that associated tag name).
     */
    @WithDefault(DEFAULT_SCHEMA_DEFINITIONS_TAG_NAME)
    String schemaDefinitionsTagName();

    /**
     * The generatedSamplesMaxDepth option controls how many schema levels automatically generated for payload samples. The
     * default is 8 which works well for most APIs, but you can adjust it if necessary for your use case.
     */
    @ConfigDocDefault("8")
    Optional<Integer> generatedSamplesMaxDepth();

    /**
     * In complex data structures or object schemas where properties are nested within parent objects the hidePropertiesPrefix
     * option enables the hiding of parent names for nested properties within the documentation.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> hidePropertiesPrefix();

    /**
     * A list of schema names to ignore. Matching schemas are excluded from the documentation. Multiple schema names can be
     * specified.
     */
    Optional<Set<String>> ignoreNamedSchemas();

    /**
     * Hides the loading animation.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> hideLoading();

    /**
     * Hides the sidebar navigation menu.
     */
    @ConfigDocDefault("false")
    Optional<Boolean> hideSidebar();
}
