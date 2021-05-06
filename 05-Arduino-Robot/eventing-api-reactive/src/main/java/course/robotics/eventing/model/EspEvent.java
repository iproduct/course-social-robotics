package course.robotics.eventing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EspEvent {
    private String id;
    @NonNull
    private String sensorId;
    private final long timestamp;
    private final float distance;
}
