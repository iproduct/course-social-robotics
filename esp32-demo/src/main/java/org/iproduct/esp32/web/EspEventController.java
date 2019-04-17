package org.iproduct.esp32.web;

import org.iproduct.esp32.model.EspEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/events")
public class EspEventController {

    @GetMapping
    public List<EspEvent> getEvents() {
        return Arrays.asList(
            new EspEvent(12),
            new EspEvent(25),
            new EspEvent(9)
        );
    }
}
