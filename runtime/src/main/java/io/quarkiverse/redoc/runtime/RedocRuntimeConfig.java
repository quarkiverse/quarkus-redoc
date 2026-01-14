package io.quarkiverse.redoc.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.redoc")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface RedocRuntimeConfig {

    /**
     * Whether Redoc is enabled at runtime.
     * If false, the Redoc UI will return a 404 response.
     */
    @WithDefault("true")
    boolean enabled();
}
