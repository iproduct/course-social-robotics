package course.robotics.eventing.webclient;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

public class ReactiveWebSocketClient {

    public static void main(String[] args) throws InterruptedException {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(
                URI.create("ws://192.168.0.12:8080/event-emitter"),
                session -> session.send(
                        Mono.just(session.textMessage("event-spring-reactive-client-websocket")))
                        .thenMany(session.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .log())
                        .then())
                .block(Duration.ofSeconds(60L));
    }
}
