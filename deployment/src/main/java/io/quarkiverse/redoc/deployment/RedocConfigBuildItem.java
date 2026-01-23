package io.quarkiverse.redoc.deployment;

import io.quarkiverse.redoc.deployment.model.RedocConfigDto;
import io.quarkus.builder.item.SimpleBuildItem;

/**
 * Build item containing the resolved Redoc configuration with defaults applied.
 */
public final class RedocConfigBuildItem extends SimpleBuildItem {

    private final RedocConfigDto config;

    public RedocConfigBuildItem(RedocConfigDto config) {
        this.config = config;
    }

    public RedocConfigDto getConfig() {
        return config;
    }
}
