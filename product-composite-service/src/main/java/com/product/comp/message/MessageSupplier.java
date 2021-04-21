/**
 * 
 */
package com.product.comp.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.product.service.api.core.product.ProductApi;
import com.product.service.api.event.DataEvent;
import com.product.service.api.event.EventType;

import java.util.function.Supplier;

/**
 * @author Gbenga
 *
 */
@Component
public class MessageSupplier {

	private final Logger LOG = LoggerFactory.getLogger(MessageSupplier.class);

	private Boolean produce;

	public MessageSupplier(@Value("${spring.cloud.stream.producer.produce}") Boolean produce) {
		this.produce = produce;
	}

	@Bean
	public Supplier<DataEvent<String, ProductApi>> productProducer() {
		return () -> {
			if (produce) {
				return getUserPayload();
				//return MessageBuilder.withPayload(getUserPayload())
						//.setHeader("to_process", true).build();
			}
			return null;
		};
	}

	private DataEvent<String, ProductApi> getUserPayload() {

		ProductApi product = new ProductApi(465, "aminat2z2", 365, "description", null);
		return new DataEvent<>(EventType.CREATE, null, product);
	}
}
