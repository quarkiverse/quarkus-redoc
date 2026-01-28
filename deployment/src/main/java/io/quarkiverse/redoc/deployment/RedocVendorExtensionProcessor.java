package io.quarkiverse.redoc.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;

/**
 * Processor for adding Redoc CE vendor extensions to the OpenAPI document.
 */
class RedocVendorExtensionProcessor {

    @BuildStep
    void addXLogoToOpenAPI(
            BuildProducer<AddToOpenAPIDefinitionBuildItem> openApiProducer,
            RedocConfigBuildItem redocConfigBuildItem) {

        if (redocConfigBuildItem.getConfig().extensionsModel().xLogoModel() == null) {
            return;
        }

        // Create and register the OASFilter
        XLogoOASFilter filter = new XLogoOASFilter(redocConfigBuildItem.getConfig().extensionsModel().xLogoModel());

        openApiProducer.produce(new AddToOpenAPIDefinitionBuildItem(filter));
    }
}
