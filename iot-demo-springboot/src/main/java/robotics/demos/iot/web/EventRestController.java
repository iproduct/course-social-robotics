package robotics.demos.iot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import robotics.demos.iot.dao.EventRepository;
import robotics.demos.iot.model.Event;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventRestController {
    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event created = eventRepository.save(event);
        log.info("Event created: {}", created);
        URI uri = MvcUriComponentsBuilder.fromMethodName(EventRestController.class, "createEvent", Event.class)
                .pathSegment("{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
