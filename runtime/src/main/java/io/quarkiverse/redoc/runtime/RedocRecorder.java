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

    public Handler<RoutingContext> createLogoHandler(String resourcePath) {
        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                if (!config.getValue().enabled()) {
                    event.response().setStatusCode(404);
                    event.response().end();
                    return;
                }

                try (InputStream is = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(resourcePath)) {
                    if (is == null) {
                        event.response().setStatusCode(404);
                        event.response().end();
                        return;
                    }

                    // Check file size to prevent memory issues
                    byte[] bytes = is.readNBytes(5 * 1024 * 1024); // Limit to 5MB
                    if (is.read() != -1) {
                        // File is larger than 5MB
                        event.response().setStatusCode(413); // Payload Too Large
                        event.response().end();
                        return;
                    }

                    String contentType = determineContentType(resourcePath);

                    event.response()
                            .putHeader("Content-Type", contentType)
                            .putHeader("Content-Length", String.valueOf(bytes.length))
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
