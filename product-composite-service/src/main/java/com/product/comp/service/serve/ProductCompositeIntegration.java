package com.product.comp.service.serve;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.Output;

import org.springframework.cloud.stream.function.StreamBridge;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.product.comp.message.MyMessageHeaders;
import com.product.service.api.core.product.ProductApi;
import com.product.service.api.core.product.ProductService;
import com.product.service.api.core.recommendation.Recommendation;
import com.product.service.api.core.recommendation.RecommendationService;
import com.product.service.api.core.review.Review;
import com.product.service.api.core.review.ReviewService;
import com.product.service.api.event.DataEvent;
import com.product.service.api.event.Event;
import com.product.service.api.event.EventType;
import com.product.service.util.exceptions.BadRequestException;
import com.product.service.util.exceptions.InvalidInputException;
import com.product.service.util.exceptions.NotFoundException;
import com.product.service.util.http.HttpErrorInfo;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static reactor.core.publisher.Flux.empty;
import static com.product.service.api.event.Event.Type.CREATE;
import static com.product.service.api.event.Event.Type.DELETE;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@EnableBinding(ProductCompositeIntegration.MessageSources.class)
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

	private final WebClient webClient;
	private final ObjectMapper mapper;

	private final String productServiceUrl;
	private final String recommendationServiceUrl;
	private final String reviewServiceUrl;

	private static final String OUTPUT_PRODUCTS = "productProducer-out-0";
	private static final String OUTPUT_RECOMMENDATIONS = "recommendationProducer-out-0";
	private static final String OUTPUT_REVIEWS = "reviewProducer-out-0";

	// private MessageSources messageSources;
	private final StreamBridge streamBridge;
	private Boolean toProcess; 

	/*
	public interface MessageSources {

		@Output(OUTPUT_PRODUCTS)
		MessageChannel outputProducts();

		@Output(OUTPUT_RECOMMENDATIONS)
		MessageChannel outputRecommendations();

		@Output(OUTPUT_REVIEWS)
		MessageChannel outputReviews();

	}
	*/

	@Autowired
	public ProductCompositeIntegration(WebClient.Builder webClient, ObjectMapper mapper,
			/* MessageSources messageSources,*/
			StreamBridge streamBridge,

			@Value("${app.product-service.host}") String productServiceHost,
			@Value("${app.product-service.port}") int productServicePort,

			@Value("${app.recommendation-service.host}") String recommendationServiceHost,
			@Value("${app.recommendation-service.port}") int recommendationServicePort,

			@Value("${app.review-service.host}") String reviewServiceHost,
			@Value("${app.review-service.port}") int reviewServicePort,
			@Value("${producer.supplier.enabled}") Boolean toProcess) {

		this.webClient = webClient.build();
		this.mapper = mapper;
		//this.messageSources = messageSources;
		this.streamBridge = streamBridge;
		this.toProcess = toProcess;
		
		productServiceUrl = "http://" + productServiceHost + ":" + productServicePort;
		recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort;
		reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort;
	}

	@Override
	public Mono<ProductApi> createProduct(ProductApi productPayload) {
		DataEvent<String, ProductApi> eventPayload = new DataEvent<>(EventType.CREATE, null, productPayload);
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_PRODUCTS, createEventMessage(eventPayload, headers));

		return sent ? Mono.just(productPayload) : Mono.error(new BadRequestException("Error streaming data"));
		
		//messageSources.outputProducts()
			//	.send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
		//return body;
	}

	@Override
	public Mono<ProductApi> getProduct(int productId) {
		String url = productServiceUrl + "/product/" + productId;
		log.debug("Will call the getProduct API on URL: {}", url);

		return webClient.get().uri(url).retrieve().bodyToMono(ProductApi.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
	}

	@Override
	public Mono<Void> deleteProduct(int productId) {
		DataEvent<String, ProductApi> eventPayload = new DataEvent<>(EventType.DELETE, String.valueOf(productId), null);
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_PRODUCTS, createEventMessage(eventPayload, headers));
		        
        return Mono.empty();
		//messageSources.outputProducts().send(MessageBuilder.withPayload(new Event(DELETE, productId, null)).build());
	}

	@Override
	public Mono<Recommendation> createRecommendation(Recommendation recommendationPayload) {
		DataEvent<String, Recommendation> eventPayload = new DataEvent<>(EventType.CREATE, null, recommendationPayload);
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_RECOMMENDATIONS, createEventMessage(eventPayload, headers));

		return sent ? Mono.just(recommendationPayload) : Mono.error(new BadRequestException("Error streaming data"));
		
		//messageSources.outputRecommendations()
				//.send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
		//return body;
	}

	@Override
	public Flux<Recommendation> getRecommendations(int productId) {

		String url = recommendationServiceUrl + "/recommendation?productId=" + productId;

		log.debug("Will call the getRecommendations API on URL: {}", url);

		// Return an empty result if something goes wrong to make it possible for the
		// composite service to return partial responses
		return webClient.get().uri(url).retrieve().bodyToFlux(Recommendation.class).log()
				.onErrorResume(error -> empty());
	}

	@Override
	public Mono<Void> deleteRecommendations(int productId) {
		DataEvent<String, Recommendation> eventPayload = new DataEvent<>(EventType.DELETE, String.valueOf(productId), null);
		
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_RECOMMENDATIONS, createEventMessage(eventPayload, headers));
        
        return Mono.empty();
        
		//messageSources.outputRecommendations()
			//	.send(MessageBuilder.withPayload(new Event(DELETE, productId, null)).build());
	}

	@Override
	public Mono<Review> createReview(Review reviewPayload) {
		
		DataEvent<String, Review> eventPayload = new DataEvent<>(EventType.CREATE, null, reviewPayload);		
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_REVIEWS, createEventMessage(eventPayload, headers));
		return sent ? Mono.just(reviewPayload) : Mono.error(new BadRequestException("Error streaming data"));
		
		//messageSources.outputReviews()
			//	.send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
		//return body;
	}

	@Override
	public Flux<Review> getReviews(int productId) {

		String url = reviewServiceUrl + "/review?productId=" + productId;

		log.debug("Will call the getReviews API on URL: {}", url);

		// Return an empty result if something goes wrong to make it possible for the
		// composite service to return partial responses
		return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log().onErrorResume(error -> empty());

	}

	@Override
	public Mono<Void> deleteReviews(int productId) {
		DataEvent<String, Review> eventPayload = new DataEvent<>(EventType.DELETE, String.valueOf(productId), null);
		Map<String, Object> headers = new HashMap<>();
		headers.put("to_process", toProcess);		
				
		boolean sent = streamBridge.send(OUTPUT_REVIEWS, createEventMessage(eventPayload, headers));
        
        return Mono.empty();
		// messageSources.outputReviews().send(MessageBuilder.withPayload(new Event(DELETE, productId, null)).build());
	}
	
	private <K, T> Message<DataEvent<K, T>> createEventMessage(DataEvent<K, T> eventPayload, Map<String, Object> headers) {
		
		return MessageBuilder.createMessage(eventPayload, MyMessageHeaders.createHeaders(headers));
	}

	public Mono<Health> getProductHealth() {
		return getHealth(productServiceUrl);
	}

	public Mono<Health> getRecommendationHealth() {
		return getHealth(recommendationServiceUrl);
	}

	public Mono<Health> getReviewHealth() {
		return getHealth(reviewServiceUrl);
	}

	private Mono<Health> getHealth(String url) {
		url += "/actuator/health";
		log.debug("Will call the Health API on URL: {}", url);
		return webClient.get().uri(url).retrieve().bodyToMono(String.class).map(s -> new Health.Builder().up().build())
				.onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build())).log();
	}

	private Throwable handleException(Throwable ex) {

		if (!(ex instanceof WebClientResponseException)) {
			log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
			return ex;
		}

		WebClientResponseException wcre = (WebClientResponseException) ex;

		switch (wcre.getStatusCode()) {

		case NOT_FOUND:
			return new NotFoundException(getErrorMessage(wcre));

		case UNPROCESSABLE_ENTITY:
			return new InvalidInputException(getErrorMessage(wcre));

		default:
			log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
			log.warn("Error body: {}", wcre.getResponseBodyAsString());
			return ex;
		}
	}

	private String getErrorMessage(WebClientResponseException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}
}