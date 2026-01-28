package io.quarkiverse.redoc.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

/**
 * Test that the x-logo configuration is properly handled.
 * Note: This test only validates that the extension doesn't break the build.
 * The actual functionality testing is done in integration tests.
 */
public class XLogoConfigTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(new StringAsset("""
                            quarkus.redoc.x-logo.url=https://example.com/logo.png
                            quarkus.redoc.x-logo.background-color=#FFFFFF
                            quarkus.redoc.x-logo.alt-text=Example Logo
                            quarkus.redoc.x-logo.href=https://example.com
                            """), "application.properties"));

    @Test
    void testXLogoConfigLoads() {
        // Test that the application starts with x-logo configuration
        assertNotNull(unitTest);
    }
}
