package io.quarkiverse.redoc.deployment;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Info;

/**
 * OpenAPI filter that adds the x-logo vendor extension to the Info object.
 */
public class XLogoOASFilter implements OASFilter {

    private final String logoUrl;
    private final String backgroundColor;
    private final String altText;
    private final String href;

    public XLogoOASFilter(String logoUrl, String backgroundColor, String altText, String href) {
        this.logoUrl = logoUrl;
        this.backgroundColor = backgroundColor;
        this.altText = altText;
        this.href = href;
    }

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (openAPI == null) {
            return;
        }

        // If Info doesn't exist, create it
        Info info = openAPI.getInfo();
        if (info == null) {
            info = org.eclipse.microprofile.openapi.OASFactory.createInfo();
            openAPI.setInfo(info);
        }

        // Build the x-logo extension object
        Map<String, Object> xLogo = new HashMap<>();
        xLogo.put("url", logoUrl);

        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            xLogo.put("backgroundColor", backgroundColor);
        }

        if (altText != null && !altText.isEmpty()) {
            xLogo.put("altText", altText);
        }

        if (href != null && !href.isEmpty()) {
            xLogo.put("href", href);
        }

        // Add the x-logo extension to the Info object
        info.addExtension("x-logo", xLogo);
    }
}
