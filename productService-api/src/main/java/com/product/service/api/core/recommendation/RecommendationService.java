package com.product.service.api.core.recommendation;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {

    Mono<Recommendation> createRecommendation(@RequestBody Recommendation body);

    /**
     * Sample usage:
     *
     * curl $HOST:$PORT/recommendation?productId=1
     *
     * @param productId
     * @return
     */
    @GetMapping( value    = "/recommendation", produces = "application/json")
    Flux<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

    Mono<Void> deleteRecommendations(@RequestParam(value = "productId", required = true)  int productId);
}
