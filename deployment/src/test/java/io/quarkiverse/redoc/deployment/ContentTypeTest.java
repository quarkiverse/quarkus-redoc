package io.quarkiverse.redoc.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URLConnection;

import org.junit.jupiter.api.Test;

public class ContentTypeTest {

    @Test
    public void testContentTypeDetection() {
        // Test common image formats
        assertEquals("image/png", getContentType("logo.png"));
        assertEquals("image/jpeg", getContentType("logo.jpg"));
        assertEquals("image/jpeg", getContentType("logo.jpeg"));
        assertEquals("image/gif", getContentType("logo.gif"));

        // Test with paths
        assertEquals("image/png", getContentType("redoc-logo.png"));
        assertEquals("image/jpeg", getContentType("path/to/image.jpg"));

        // Test fallback for unknown types
        String unknownType = getContentType("file.unknown");
        assertTrue(unknownType == null || "application/octet-stream".equals(unknownType),
                "Unknown file types should return null or application/octet-stream");
    }

    private String getContentType(String resourcePath) {
        return URLConnection.getFileNameMap().getContentTypeFor(resourcePath);
    }
}
