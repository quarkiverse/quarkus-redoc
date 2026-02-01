package io.quarkiverse.redoc.deployment.model;

import java.util.List;

/**
 * DTO representing a single tag group in the x-tagGroups OpenAPI vendor extension.
 */
public class XTagGroupModel {
    public String name;
    public List<String> tags;
}
