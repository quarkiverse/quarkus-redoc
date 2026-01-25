package io.quarkiverse.redoc.deployment.model;

import java.util.List;
import java.util.Set;

import io.quarkiverse.redoc.deployment.config.Layout;

/**
 * DTO representing the resolved Redoc configuration with defaults applied.
 */
public record RedocConfigModel(
        String path,
        String routingBasePath,
        String title,
        boolean alwaysInclude,
        Boolean hideDownloadButtons,
        Boolean hideSchemaTitles,
        String jsonSamplesExpandLevel,
        Integer maxDisplayedEnumValues,
        Layout layout,
        Boolean onlyRequiredInSamples,
        Boolean sortRequiredPropsFirst,
        String schemasExpansionLevel,
        Integer scrollYOffset,
        String showExtensions,
        Boolean sanitize,
        List<DownloadUrlModel> downloadUrlModels,
        String schemaDefinitionsTagName,
        Integer generatedSamplesMaxDepth,
        Boolean hidePropertiesPrefix,
        Set<String> ignoreNamedSchemas,
        Boolean hideLoading,
        Boolean hideSidebar) {
}
