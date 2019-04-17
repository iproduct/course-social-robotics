package org.iproduct.esp32.web;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.esp32.model.EspEvent;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("api/events")
@Slf4j
public class EspEventController {

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
    public EspEvent addEvent(@RequestBody EspEvent event) {
        log.info("Event received:" + event.toString());
        events.add(event);
        return event;
    }
}
