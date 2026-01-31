package io.quarkiverse.redoc.deployment.model;

import java.util.List;

public record ExtensionsModel(
        XLogoModel xLogoModel,
        List<XTagGroupModel> xTagGroupModels,
        String xTagGroupsUngroupedName) {
}
