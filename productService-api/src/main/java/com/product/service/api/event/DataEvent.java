/**
 * 
 */
package com.product.service.api.event;

import java.time.LocalDateTime;

/**
 * @author Gbenga
 *
 */
public class DataEvent<K, T> {

    private K key;
    private T data;
    private EventType eventType;
    private LocalDateTime eventCreatedAt;

    public DataEvent() {
        this.key = null;
        this.data = null;
        this.eventType = null;
        this.eventCreatedAt = null;
    }

    public DataEvent(EventType eventType, K key, T data) {
        this.key = key;
        this.data = data;
        this.eventType = eventType;
        this.eventCreatedAt = LocalDateTime.now();
    }

    public K getKey() {
        return key;
    }

    public T getData() {
        return data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public LocalDateTime getEventCreatedAt() {
        return eventCreatedAt;
    }
}
