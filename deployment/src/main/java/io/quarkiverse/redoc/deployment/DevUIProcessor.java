package io.quarkiverse.redoc.deployment;

import java.util.Optional;

import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.devui.spi.DevContextBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.smallrye.openapi.common.deployment.SmallRyeOpenApiConfig;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.runtime.management.ManagementInterfaceBuildTimeConfig;

public class DevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createCard(NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem,
            ManagementInterfaceBuildTimeConfig managementBuildTimeConfig,
            LaunchModeBuildItem launchModeBuildItem,
            BuildProducer<CardPageBuildItem> cardPageBuildItemBuildProducer,
            Optional<DevContextBuildItem> devContextBuildItem,
            RedocConfig redocConfig,
            SmallRyeOpenApiConfig openApiConfig) {
        final CardPageBuildItem card = new CardPageBuildItem();

        String devUIContextRoot;
        if (devContextBuildItem.isPresent()) {
            devUIContextRoot = devContextBuildItem.get().getDevUIContextRoot();
        } else {
            devUIContextRoot = "";
        }
        String uiPath = devUIContextRoot + nonApplicationRootPathBuildItem.resolveManagementPath(redocConfig.path(),
                managementBuildTimeConfig, launchModeBuildItem, openApiConfig.managementEnabled());

        card.addPage(Page.externalPageBuilder("Redoc CE")
                .url(uiPath, uiPath)
                .isHtmlContent()
                .doNotEmbed(true)
                .icon("font-awesome-solid:signs-post"));

        cardPageBuildItemBuildProducer.produce(card);
    }

}
