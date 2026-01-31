package io.quarkiverse.redoc.deployment.config;

import java.util.List;

/**
 * Configuration for a single tag group in the x-tagGroups OpenAPI vendor extension.
 * <p>
 * This extension allows grouping tags in the sidebar navigation.
 * </p>
 */
public interface XTagGroupConfig {
    /**
     * The name of the tag group displayed in the sidebar.
     */
    String name();

    /**
     * The list of tag names to include in this group.
     */
    List<String> tags();
}
