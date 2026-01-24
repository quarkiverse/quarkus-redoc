package io.quarkiverse.redoc.deployment;

import io.quarkiverse.redoc.deployment.model.RedocConfigModel;
import io.quarkus.builder.item.SimpleBuildItem;

/**
 * Build item containing the resolved Redoc configuration with defaults applied.
 * <p/>
 * Not intended to be consumed by other extensions right now.
 */
public final class RedocConfigBuildItem extends SimpleBuildItem {

    private final RedocConfigModel config;

    public RedocConfigBuildItem(RedocConfigModel config) {
        this.config = config;
    }

    public RedocConfigModel getConfig() {
        return config;
    }
}
