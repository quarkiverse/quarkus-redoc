package io.quarkiverse.redoc.deployment.config;

import java.util.List;
import java.util.Optional;

import io.smallrye.config.WithDefault;

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

    /**
     * Configuration for the x-tagGroups OpenAPI vendor extension.
     * <p>
     * This extension allows grouping tags in the sidebar navigation.
     * </p>
     */
    List<XTagGroupConfig> xTagGroups();

    /**
     * Name for the group that contains any tags not explicitly assigned to a tag group.
     * <p>
     * When x-tag-groups is configured, tags not assigned to any group are hidden by Redoc.
     * This option creates an additional group containing all ungrouped tags.
     * </p>
     * Set to an empty string - or null depending on your config source - to disable this group.
     */
    @WithDefault("Other")
    Optional<String> xTagGroupsUngroupedName();
}
