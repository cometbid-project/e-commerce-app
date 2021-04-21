package com.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

import com.product.service.api.core.product.ProductApi;
import com.product.service.api.core.product.ProductService;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.cloud.stream.messaging.Sink;
import com.product.service.api.core.recommendation.Recommendation;
import com.product.service.api.core.recommendation.RecommendationService;
import com.product.service.api.event.DataEvent;
import com.product.service.api.event.Event;
import com.product.service.util.exceptions.EventProcessingException;

//@EnableBinding(Sink.class)
@Component
public class MessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

	private final ProductService productService;

	@Autowired
	public MessageProcessor(ProductService recommendationService) {
		this.productService = recommendationService;
	}

	// @StreamListener(target = Sink.INPUT)
	// public void process(Event<Integer, Recommendation> event) {
	@Bean
	public Consumer<DataEvent<String, ProductApi>> productConsumer() {

		return event -> {
			LOG.info("Process message created at {}...", event.getEventCreatedAt());

			switch (event.getEventType()) {

			case CREATE:
				ProductApi product = event.getData();
				LOG.info("Create recommendation with ID: {}/{}", product.getProductId(),
						product.getProductId());
				productService.createProduct(product);
				break;

			case DELETE:
				int productId = Integer.parseInt(event.getKey());
				LOG.info("Delete recommendations with ProductID: {}", productId);
				productService.deleteProduct(productId);
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
