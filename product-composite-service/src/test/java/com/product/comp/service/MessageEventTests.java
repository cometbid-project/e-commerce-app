/**
 * 
 */
package com.product.comp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.service.api.composite.prod.ProductCompositeService;
import com.product.service.api.core.product.ProductApi;
import com.product.service.api.event.DataEvent;
import com.product.service.api.event.EventType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
/**
 * @author Gbenga
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
public class MessageEventTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductCompositeService service;

	/*
	 * ONLY Works with a Consumer and a Function
	 * 
	 * @Autowired private InputDestination input;
	 */

	@Autowired
	OutputDestination output;

	@Autowired
	ObjectMapper mapper;

	@Test
	void sendCreateUserEventTest() {

		int productId = 645;
		int weight = 123;
		String description = "test@test.com";
		String name = "Test Test";
		String serviceAddress = "535.85.856.34";

		ProductApi userPayload = new ProductApi(productId, name, weight, description, serviceAddress);
		DataEvent<String, ProductApi> event = new DataEvent<>(EventType.CREATE, null, userPayload);

		// input.send(MessageBuilder.withPayload(event).build()); //Can only work with a
		// Consumer or Function
		postAndVerifyProduct(userPayload);

		// Message<byte[]> received = output.receive(0, "userConsumer-out-0"); // for
		// more suppliers
		Message<byte[]> received = output.receive(); // Using OutputDestination

		Assertions.assertNotNull(received);

		byte[] payload = received.getPayload();
		Assertions.assertNotNull(payload);
		Assertions.assertTrue(payload.length > 0);

		DataEvent<String, ProductApi> receivedEvent = deserialize(payload);
		Assertions.assertNotNull(receivedEvent);
		Assertions.assertEquals(receivedEvent.getEventType(), EventType.CREATE);
		Assertions.assertNull(receivedEvent.getKey());

		ProductApi receivedData = receivedEvent.getData();
		Assertions.assertNotNull(receivedData);
		Assertions.assertEquals(weight, receivedData.getWeight());
		Assertions.assertEquals(description, receivedData.getDescription());
		Assertions.assertEquals(name, receivedData.getName());
	}

	@Test
	void sendDeleteUserEventTest() {

		String productId = "product-id-to-be-deleted";
		DataEvent<String, ProductApi> event = new DataEvent<>(EventType.DELETE, productId, null);

		// input.send(MessageBuilder.withPayload(event).build()); //Can only work with a
		// Consumer or Function
		deleteAndVerifyProduct(productId);

		// Message<byte[]> received = output.receive(0, "userConsumer-out-0"); // for
		// more suppliers
		Message<byte[]> received = output.receive(); // Using OutputDestination

		Assertions.assertNotNull(received);

		byte[] payload = received.getPayload();
		Assertions.assertNotNull(payload);
		Assertions.assertTrue(payload.length > 0);

		DataEvent<String, ProductApi> receivedEvent = deserialize(payload);
		Assertions.assertNotNull(receivedEvent);
		Assertions.assertEquals(EventType.DELETE, receivedEvent.getEventType());
		Assertions.assertEquals(productId, receivedEvent.getKey());
		Assertions.assertNull(receivedEvent.getData());
	}

	private void postAndVerifyProduct(ProductApi productPayload) {
		client.post().uri("/product-service").body(Mono.just(productPayload), ProductApi.class).exchange()
				.expectStatus().isOk();
	}

	private void deleteAndVerifyProduct(String productId) {
		client.delete().uri("/product-service" + productId).exchange().expectStatus().isOk();
	}

	private DataEvent<String, ProductApi> deserialize(byte[] payload) {
		DataEvent<String, ProductApi> dataEvent = null;
		try {
			dataEvent = mapper.readValue(payload, new TypeReference<>() {
			});
		} catch (IOException i) {
			System.out.println(i.getMessage());
		}

		return dataEvent;
	}
}
