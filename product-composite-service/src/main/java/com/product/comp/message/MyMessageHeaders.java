/**
 * 
 */
package com.product.comp.message;

import java.util.Map;
import java.util.UUID;
import org.springframework.messaging.MessageHeaders;

import lombok.Builder;

/**
 * @author Gbenga
 *
 */
@SuppressWarnings("serial")
public class MyMessageHeaders extends MessageHeaders {

	private MyMessageHeaders(Map<String, Object> headers) {
		super(headers, UUID.randomUUID(), System.currentTimeMillis());
		// TODO Auto-generated constructor stub
	}

	public static MyMessageHeaders createHeaders(Map<String, Object> headers) {

		return new MyMessageHeaders(headers);
	}

}
