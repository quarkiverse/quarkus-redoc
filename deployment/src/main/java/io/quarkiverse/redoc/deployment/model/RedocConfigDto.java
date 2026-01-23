package io.quarkiverse.redoc.deployment.model;

import java.util.List;

import io.quarkiverse.redoc.deployment.config.Layout;

/**
 * DTO representing the resolved Redoc configuration with defaults applied.
 */
public record RedocConfigDto(
        String path,
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
        List<DownloadUrlDto> downloadUrls,
        String schemaDefinitionsTagName,
        Integer generatedSamplesMaxDepth,
        Boolean hidePropertiesPrefix) {
}
