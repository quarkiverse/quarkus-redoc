package io.quarkiverse.redoc.deployment.config;

/**
 * Layout mode options for Redoc documentation.
 */
public enum Layout {
    /**
     * Default three-panel layout with sidebar, content, and code samples.
     */
    THREE_PANEL("three-panel"),

    /**
     * Stacked layout, better for narrow screens.
     */
    STACKED("stacked");

    private final String value;

    Layout(String value) {
        this.value = value;
    }

    /**
     * Returns the Redoc configuration value for this layout.
     */
    public String getValue() {
        return value;
    }
}
