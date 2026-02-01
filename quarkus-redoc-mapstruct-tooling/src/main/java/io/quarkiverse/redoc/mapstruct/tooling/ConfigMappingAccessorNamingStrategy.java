package io.quarkiverse.redoc.mapstruct.tooling;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;

/**
 * AccessorNamingStrategy which tells mapstruct how to deal with ConfigMap like interfaces.
 */
public class ConfigMappingAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

    @Override
    public boolean isGetterMethod(ExecutableElement method) {
        if (method.getEnclosingElement().getKind() != ElementKind.INTERFACE) {
            return false;
        }

        if (method.isDefault()) {
            return false;
        }

        // For ConfigMapping-like interfaces, all no-arg methods are getters
        return method.getParameters().isEmpty();
    }

    @Override
    public String getPropertyName(ExecutableElement method) {
        // For interfaces, the method name IS the property name (e.g., path() -> path)
        // isGetterMethod already decides if this method should be used as property
        return method.getSimpleName().toString();
    }

    @Override
    public boolean isSetterMethod(ExecutableElement method) {
        return false;
    }

    @Override
    protected boolean isFluentSetter(ExecutableElement method) {
        return false;
    }
}
