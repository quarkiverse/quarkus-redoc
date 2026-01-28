package io.quarkiverse.redoc.runtime;

import java.io.InputStream;
import java.net.URLConnection;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class RedocRecorder {

    private final RuntimeValue<RedocRuntimeConfig> config;

    public RedocRecorder(RuntimeValue<RedocRuntimeConfig> config) {
        this.config = config;
    }

    public Handler<RoutingContext> createHandler(String htmlContent) {
        if (!config.getValue().enabled()) {
            return _404handler();
        }

        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                event.response().end(htmlContent);
            }
        };
    }

    private Handler<RoutingContext> _404handler() {
        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                event.response().setStatusCode(404);
                event.response().end();
            }
        };
    }

    public Handler<RoutingContext> createLogoHandler(String resourcePath) {
        if (!config.getValue().enabled()) {
            return _404handler();
        }

        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                try (InputStream is = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(resourcePath)) {
                    if (is == null) {
                        event.response().setStatusCode(404);
                        event.response().end();
                        return;
                    }

                    byte[] bytes = is.readAllBytes();
                    String contentType = determineContentType(resourcePath);

                    event.response()
                            .putHeader("Content-Type", contentType)
                            .putHeader("Content-Length", String.valueOf(bytes.length))
                            .putHeader("Cache-Control", "public, max-age=86400") // Cache for 1 day
                            .end(Buffer.buffer(bytes));
                } catch (Exception e) {
                    event.response().setStatusCode(500);
                    event.response().end();
                }
            }
        };
    }

    private String determineContentType(String resourcePath) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(resourcePath);
        return contentType != null ? contentType : "application/octet-stream";
    }
}
