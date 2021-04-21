package com.product.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import com.product.service.api.core.product.ProductService;
import com.product.persistence.ProductRepo;
import com.product.persistence.ProductEntity;
import com.product.service.util.http.ServiceUtil;

import com.product.service.api.core.product.ProductApi;
import com.product.service.util.exceptions.InvalidInputException;
import com.product.service.util.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import static reactor.core.publisher.Mono.error;


/**
 *
 * @author Adedayo
 */

@Slf4j
@RestController
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepo repository;
    
    private final ProductMapper mapper;
    
    private final ServiceUtil serviceUtil;
    
    @Autowired
    ProductServiceImpl(ProductRepo repository, ProductMapper mapper, ServiceUtil serviceUtil){
        this.mapper = mapper;
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<ProductApi> createProduct(ProductApi body) {
    	
        if (body.getProductId() < 1) 
        	throw new InvalidInputException("Invalid productId: " + body.getProductId());

        ProductEntity entity = mapper.apiToEntity(body);
        Mono<ProductApi> newEntity = repository.save(entity)
            .log().onErrorMap( DuplicateKeyException.class,
              ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
            .map(e -> mapper.entityToApi(e));

        return newEntity;
    }

    @Override
    public Mono<ProductApi> getProduct(int productId) {
       if (productId < 1) 
    	   throw new InvalidInputException("Invalid productId: " + productId);

        return repository.findByProductId(productId)
            .switchIfEmpty(error(new NotFoundException("No product found for productId: " + productId)))
            .log().map(e -> mapper.entityToApi(e))
            .map(e -> {e.setServiceAddress(serviceUtil.getServiceAddress()); return e;});
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        repository.findByProductId(productId).log().map(e -> repository.delete(e)).flatMap(e -> e).subscribe();
        
        return Mono.empty();
    }
    
    
    
}
