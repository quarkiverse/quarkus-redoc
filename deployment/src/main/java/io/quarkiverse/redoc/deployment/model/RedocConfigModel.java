package io.quarkiverse.redoc.deployment.model;

import java.util.List;
import java.util.Set;

import io.quarkiverse.redoc.deployment.config.Layout;

/**
 * DTO representing the resolved Redoc configuration with defaults applied.
 */
public class RedocConfigModel {
    public String path;
    public String routingBasePath;
    public String title;
    public boolean alwaysInclude;
    public Boolean hideDownloadButtons;
    public Boolean hideSchemaTitles;
    public String jsonSamplesExpandLevel;
    public Integer maxDisplayedEnumValues;
    public Layout layout;
    public Boolean onlyRequiredInSamples;
    public Boolean sortRequiredPropsFirst;
    public String schemasExpansionLevel;
    public Integer scrollYOffset;
    public String showExtensions;
    public Boolean sanitize;
    public List<DownloadUrlModel> downloadUrls;
    public String schemaDefinitionsTagName;
    public Integer generatedSamplesMaxDepth;
    public Boolean hidePropertiesPrefix;
    public Set<String> ignoreNamedSchemas;
    public Boolean hideLoading;
    public Boolean hideSidebar;
    public ExtensionsModel extensions;
}
