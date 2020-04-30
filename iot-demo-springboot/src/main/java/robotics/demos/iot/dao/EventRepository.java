package robotics.demos.iot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import robotics.demos.iot.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
