package io.quarkiverse.redoc.runtime;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class RedocRecorder {

    private final RuntimeValue<RedocRuntimeConfig> config;

    public RedocRecorder(RuntimeValue<RedocRuntimeConfig> config) {
        this.config = config;
    }

    public Handler<RoutingContext> createHandler(String htmlContent) {
        if (config.getValue().enabled()) {
            return new Handler<RoutingContext>() {
                @Override
                public void handle(RoutingContext event) {
                    event.response().end(htmlContent);
                }
            };
        }

        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                event.response().setStatusCode(404);
                event.response().end();
            }
        };
    }
}
