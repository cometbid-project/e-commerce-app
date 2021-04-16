package com.product.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.product.service.api.core.product.ProductApi;
import com.product.persistence.ProductEntity;
import reactor.core.publisher.Mono;

/**
 *
 * @author Adedayo
 */

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
        @Mapping(target = "serviecAddress", ignore = true)       
    })
   ProductApi entityToApi(ProductEntity entity);
   
   @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    ProductEntity apiToEntity(ProductApi api);


}
