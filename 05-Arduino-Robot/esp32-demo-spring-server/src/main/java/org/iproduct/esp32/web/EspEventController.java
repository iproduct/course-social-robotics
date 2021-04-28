package org.iproduct.esp32.web;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.esp32.model.EspEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("api/events")
@Slf4j
public class EspEventController {
    private AtomicLong nextId = new AtomicLong();

    private List<EspEvent> events = new CopyOnWriteArrayList<>();

    @GetMapping
    public List<EspEvent> getEvents() {
        return events;
//        return Arrays.asList(
//            new EspEvent(12),
//            new EspEvent(25),
//            new EspEvent(9)
//        );
    }

    @PostMapping
    public ResponseEntity<EspEvent> addEvent(@RequestBody EspEvent event) {
        log.info("Event received:" + event.toString());
        event.setId(nextId.incrementAndGet() + "");
        events.add(event);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(event.getId()).toUri()
        ).body(event);
    }
}
