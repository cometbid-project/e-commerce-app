package com.review.services;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.cloud.stream.messaging.Sink;
import com.product.service.api.core.review.Review;
import com.product.service.api.core.review.ReviewService;
import com.product.service.api.event.DataEvent;
import com.product.service.api.event.Event;
import com.product.service.util.exceptions.EventProcessingException;

//@EnableBinding(Sink.class)
@Component
public class MessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

	private final ReviewService reviewService;

	@Autowired
	public MessageProcessor(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	// @StreamListener(target = Sink.INPUT)
	@Bean
	public Consumer<DataEvent<String, Review>> reviewConsumer() {

		return event -> {
			LOG.info("Process message created at {}...", event.getEventCreatedAt());

			switch (event.getEventType()) {

			case CREATE:
				Review review = event.getData();
				LOG.info("Create review with ID: {}/{}", review.getProductId(), review.getReviewId());
				reviewService.createReview(review);
				break;

			case DELETE:
				int productId = Integer.parseInt(event.getKey());
				LOG.info("Delete reviews with ProductID: {}", productId);
				reviewService.deleteReviews(productId);
				break;

			default:
				String errorMessage = "Incorrect event type: " + event.getEventType()
						+ ", expected a CREATE or DELETE event";
				LOG.warn(errorMessage);
				throw new EventProcessingException(errorMessage);
			}

			LOG.info("Message processing done!");
		};
	}
}
