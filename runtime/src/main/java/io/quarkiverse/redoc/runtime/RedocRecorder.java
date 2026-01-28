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

        // Cache the file contents at build time
        final byte[] cachedBytes;
        final String contentType;

        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (is == null) {
                return _404handler();
            }

            cachedBytes = is.readAllBytes();
            contentType = determineContentType(resourcePath);
        } catch (Exception e) {
            return _404handler();
        }

        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                event.response()
                        .putHeader("Content-Type", contentType)
                        .putHeader("Content-Length", String.valueOf(cachedBytes.length))
                        .end(Buffer.buffer(cachedBytes));
            }
        };
    }

    private String determineContentType(String resourcePath) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(resourcePath);
        return contentType != null ? contentType : "application/octet-stream";
    }
}
