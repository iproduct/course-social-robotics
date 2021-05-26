package course.robotics.eventing.web;

import course.robotics.eventing.model.EspEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

//@RestController
@RequestMapping("api/events")
@Slf4j
public class EspEventController {
    private final AtomicLong nextId = new AtomicLong();

    private final List<EspEvent> events = new CopyOnWriteArrayList<>();

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
