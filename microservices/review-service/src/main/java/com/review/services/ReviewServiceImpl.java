package com.review.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import com.product.service.api.core.review.Review;
import com.product.service.api.core.review.ReviewService;
import com.review.persistence.ReviewEntity;
import com.review.persistence.ReviewRepository;
import com.product.service.util.exceptions.InvalidInputException;
import com.product.service.util.http.ServiceUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    private final ReviewMapper mapper;

    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Review createReview(Review body) {

        if (body.getProductId() < 1) throw new InvalidInputException("Invalid productId: " + body.getProductId());

            ReviewEntity entity = mapper.apiToEntity(body);
            Mono<Review> newEntity = repository.save(entity)
            .log()
            .onErrorMap(
                DuplicateKeyException.class,
                ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId()))
            .map(e -> mapper.entityToApi(e));

        return newEntity.block();
        
    }

    @Override
    public Flux<Review> getReviews(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

         return repository.findByProductId(productId)
            .log()
            .map(e -> mapper.entityToApi(e))
            .map(e -> {e.setServiceAddress(serviceUtil.getServiceAddress()); return e;});
    }

    @Override
    public void deleteReviews(int productId) {

        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        log.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId)).block();
    }

}