package org.iproduct.demos.spring.streamingdemos;

import org.iproduct.demos.spring.streamingdemos.handlers.ReactiveIotEventsHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class ReactiveStreamingDemosApp {

    @Bean
    public RouterFunction<ServerResponse> routes(ReactiveIotEventsHandler eventsHandler) {

        return route(GET("/api/events").and(accept(APPLICATION_STREAM_JSON)), eventsHandler::streamEvents)
            .andRoute(GET("/api/events-sse").and(accept(MediaType.TEXT_EVENT_STREAM)), eventsHandler::streamEventsSSE)
            .andRoute(GET("/api/events-json").and(accept(MediaType.TEXT_EVENT_STREAM)), eventsHandler::streamEventsJsonSSE);
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveStreamingDemosApp.class, args);
    }

}

