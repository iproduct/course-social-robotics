package org.iproduct.demos.spring.streamingdemos.udp;

public interface EventListener<T> {
    void onEvent(T event);
}
