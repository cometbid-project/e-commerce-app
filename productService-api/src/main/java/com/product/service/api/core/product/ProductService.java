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

	Mono<ProductApi> createProduct(@RequestBody ProductApi body);

	@GetMapping(value = "/product/{productId}", produces = "application/json")
	Mono<ProductApi> getProduct(@PathVariable int productId);

	Mono<Void> deleteProduct(@PathVariable int productId);
}
