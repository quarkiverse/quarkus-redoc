package io.quarkiverse.redoc.deployment;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Info;

import io.quarkiverse.redoc.deployment.model.XLogoModel;

/**
 * OpenAPI filter that adds the x-logo vendor extension to the Info object.
 */
public class XLogoOASFilter implements OASFilter {

    private final XLogoModel xLogo;

    public XLogoOASFilter(XLogoModel xLogo) {
        this.xLogo = xLogo;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (openAPI == null) {
            return;
        }

        // If Info doesn't exist, create it
        Info info = openAPI.getInfo();
        if (info == null) {
            info = OASFactory.createInfo();
            openAPI.setInfo(info);
        }

        // Build the x-logo extension object
        Map<String, Object> xLogoMap = new HashMap<>();
        xLogoMap.put("url", xLogo.url);

        if (xLogo.backgroundColor != null && !xLogo.backgroundColor.isEmpty()) {
            xLogoMap.put("backgroundColor", xLogo.backgroundColor);
        }

        if (xLogo.altText != null && !xLogo.altText.isEmpty()) {
            xLogoMap.put("altText", xLogo.altText);
        }

        if (xLogo.href != null && !xLogo.href.isEmpty()) {
            xLogoMap.put("href", xLogo.href);
        }

        // Add the x-logo extension to the Info object
        info.addExtension("x-logo", xLogoMap);
    }
}
