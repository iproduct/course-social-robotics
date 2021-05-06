package course.robotics.eventing.web;

import course.robotics.eventing.model.EspEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@RestController
@RequestMapping("api/events")
@Slf4j
public class EspEventController {
    private AtomicLong nextId = new AtomicLong();

    private List<EspEvent> events = new CopyOnWriteArrayList<>();

    @GetMapping
    public List<EspEvent> getEvents() {
        return events;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<EspEvent> addEvent(@RequestBody EspEvent event, ServerHttpRequest request) {
        log.info("Event received:" + event.toString());
        event.setId(nextId.incrementAndGet() + "");
        events.add(event);
        return ResponseEntity.created(
                UriComponentsBuilder
                    .fromUri(request.getURI()).pathSegment("{id}")
                        .buildAndExpand(event.getId()).toUri())
                    .body(event);
//                request.body(BodyExtractors.toMono(EspEvent.class))
//            .flatMap(event -> {
//                log.info("Event received:" + event.toString());
//                event.setId(nextId.incrementAndGet() + "");
//                events.add(event);
//                return created(URI.create(request.path() + "/" + event.getId()))
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(event);
//            });
    }
}
