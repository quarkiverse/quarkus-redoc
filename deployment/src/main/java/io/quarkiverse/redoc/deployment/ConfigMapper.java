package io.quarkiverse.redoc.deployment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.TargetType;

import io.quarkiverse.redoc.deployment.config.RedocConfig;
import io.quarkiverse.redoc.deployment.model.RedocConfigModel;

@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ConfigMapper {

    RedocConfigModel map(RedocConfig config);

    default <T> T unwrap(Optional<T> optional, @TargetType Class<?> type) {
        return optional.orElseGet(() -> {
            // needed, most likely because of https://github.com/mapstruct/mapstruct/issues/2843
            if (Set.class.isAssignableFrom(type)) {
                return (T) Collections.emptySet();
            } else if (List.class.isAssignableFrom(type)) {
                return (T) Collections.emptyList();
            }
            return null;
        });
    }
}
