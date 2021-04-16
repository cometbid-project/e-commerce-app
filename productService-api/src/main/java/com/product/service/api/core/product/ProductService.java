package com.product.service.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

/**
 *
 * @author Adedayo
 */
public interface ProductService {

    ProductApi createProduct(@RequestBody ProductApi body);

    @GetMapping(value = "/product/{productId}", produces = "application/json")
     Mono<ProductApi> getProduct(@PathVariable int productId);

    void deleteProduct(@PathVariable int productId);
}

