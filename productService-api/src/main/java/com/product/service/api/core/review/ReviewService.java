package com.product.service.api.core.review;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Mono<Review> createReview(@RequestBody Review body);

    /**
     * Sample usage: curl $HOST:$PORT/review?productId=1
     *
     * @param productId
     * @return
     */
    @GetMapping( value    = "/review", produces = "application/json")
    Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

    Mono<Void> deleteReviews(@RequestParam(value = "productId", required = true)  int productId);
}