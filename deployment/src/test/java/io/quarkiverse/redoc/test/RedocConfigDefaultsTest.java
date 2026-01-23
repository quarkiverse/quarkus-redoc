package io.quarkiverse.redoc.test;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

public class RedocConfigDefaultsTest extends AbstractRedocConfigTest {

    private static final String CONFIG_PATH = "config/defaults/";

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(CONFIG_PATH + "expected.json"));

    @Override
    protected String getConfigPath() {
        return CONFIG_PATH;
    }
}
