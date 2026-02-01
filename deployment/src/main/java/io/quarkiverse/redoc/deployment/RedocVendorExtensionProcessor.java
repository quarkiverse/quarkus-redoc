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

        if (redocConfigBuildItem.getConfig().extensions.xLogo == null) {
            return;
        }

        // Create and register the OASFilter
        XLogoOASFilter filter = new XLogoOASFilter(redocConfigBuildItem.getConfig().extensions.xLogo);

        openApiProducer.produce(new AddToOpenAPIDefinitionBuildItem(filter));
    }

    @BuildStep
    void addXTagGroupsToOpenAPI(
            BuildProducer<AddToOpenAPIDefinitionBuildItem> openApiProducer,
            RedocConfigBuildItem redocConfigBuildItem) {

        if (redocConfigBuildItem.getConfig().extensions.xTagGroups == null
                || redocConfigBuildItem.getConfig().extensions.xTagGroups.isEmpty()) {
            return;
        }

        // Create and register the OASFilter
        XTagGroupsOASFilter filter = new XTagGroupsOASFilter(
                redocConfigBuildItem.getConfig().extensions.xTagGroups,
                redocConfigBuildItem.getConfig().extensions.xTagGroupsUngroupedName,
                redocConfigBuildItem.getConfig().schemaDefinitionsTagName);

        openApiProducer.produce(new AddToOpenAPIDefinitionBuildItem(filter));
    }
}
