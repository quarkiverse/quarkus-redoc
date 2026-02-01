package io.quarkiverse.redoc.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.tags.Tag;

import io.quarkiverse.redoc.deployment.model.XTagGroupModel;

/**
 * OpenAPI filter that adds the x-tagGroups vendor extension to the root OpenAPI object.
 */
public class XTagGroupsOASFilter implements OASFilter {

    private final List<XTagGroupModel> xTagGroups;
    private final String ungroupedName;
    private final String schemaDefinitionsTagName;

    public XTagGroupsOASFilter(List<XTagGroupModel> xTagGroups, String ungroupedName, String schemaDefinitionsTagName) {
        this.xTagGroups = xTagGroups;
        this.ungroupedName = ungroupedName;
        this.schemaDefinitionsTagName = schemaDefinitionsTagName;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (openAPI == null || xTagGroups == null || xTagGroups.isEmpty()) {
            return;
        }

        // Build the x-tagGroups extension as a list of maps
        List<Map<String, Object>> tagGroupsList = new ArrayList<>();
        Set<String> alreadyAssignedTags = new HashSet<>();
        for (XTagGroupModel xTagGroup : xTagGroups) {
            tagGroupsList.add(toMap(xTagGroup));
            alreadyAssignedTags.addAll(xTagGroup.tags);
        }

        // Add group for ungrouped tags if configured
        if (ungroupedName != null && !ungroupedName.isEmpty() && openAPI.getTags() != null) {
            List<String> ungroupedTags = openAPI.getTags().stream().map(Tag::getName)
                    .filter(tag -> !alreadyAssignedTags.contains(tag)).collect(Collectors.toList());

            if (schemaDefinitionsTagName != null && !alreadyAssignedTags.contains(schemaDefinitionsTagName)) {
                ungroupedTags.add(schemaDefinitionsTagName);
            }

            if (!ungroupedTags.isEmpty()) {
                tagGroupsList.add(toMap(ungroupedName, ungroupedTags));
            }
        }

        // Add the x-tagGroups extension to the root OpenAPI object
        openAPI.addExtension("x-tagGroups", tagGroupsList);
    }

    private Map<String, Object> toMap(XTagGroupModel tagGroup) {
        return toMap(tagGroup.name, tagGroup.tags);
    }

    private Map<String, Object> toMap(String name, Collection<String> tags) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("tags", tags);
        return map;
    }
}
