package course.robotics.eventing.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import course.robotics.eventing.udp.UDPServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Component
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {
    @Autowired
    private UDPServer udpServer;

    @Autowired
    ObjectMapper mapper;

//    @Override
//    public Mono<Void> handle2(WebSocketSession webSocketSession) {
//        Flux<RecognitionResult> recognitions = webSocketSession.receive()
//                .map(WebSocketMessage::getPayloadAsText)
//                .map(text -> {
//                    try {
//                        return mapper.readValue(text, Resource.class);
//                    } catch (JsonProcessingException e) {
//                        log.error("Error reading value from JSON:", e);
//                        throw new RuntimeException(e);
//                    }
//                })
//                .map(resource -> embeddingsService.extractEmbeddings(resource))
//                .filter(resource -> resource != null)
//                .map(embeddings -> {
//                    try {
//                        RecognitionResult result = recognizerService.recognize(embeddings.getEmbeddings());
//                        System.out.printf("Recognition result: %s%n", result);
//                        return result;
//                    } catch (PredictException e) {
//                        log.error("Error predicting embedding: ", e);
//                        throw new RuntimeException(e);
//                    }
//                })
//                .onErrorReturn(new RecognitionResult())
//                .log().share();
//
//        Mono<Void> output = webSocketSession.send(recognitions
//                .map(RecognitionResult::toString)
//                .map(result -> {
//                    try {
//                        return mapper.writeValueAsString(result);
//                    } catch (JsonProcessingException e) {
//                        log.error("Error reading value from JSON:", e);
//                        throw new RuntimeException(e);
//                    }
//                })
//                .map(webSocketSession::textMessage));
//
//        return output;
//    }


    public Mono<Void> handle(WebSocketSession webSocketSession) {
//        return webSocketSession.send(Flux.interval(Duration.ofMillis(1000))
//                .map(n -> String.format("{\"id\": %d}", n))
        return webSocketSession.send(udpServer.getEventEmitter().onBackpressureDrop()
                .log()
                .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .doOnNext(message -> {
                            log.info("Command received: "+ message);
                            try {
                                udpServer.sendMessage(message, 1);
                            } catch (IOException e) {
                                log.error("Error sending UDP message to robot 1", e);
                            }
                        }))
                .log();
    }

}

//generator.getQuoteStream(Duration.ofMillis(5000))
//        .map(obj -> {
//        try {
//        return mapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//        log.error("Error mapping value to JSON:", e);
//        throw new RuntimeException(e);
//        }
//        })
