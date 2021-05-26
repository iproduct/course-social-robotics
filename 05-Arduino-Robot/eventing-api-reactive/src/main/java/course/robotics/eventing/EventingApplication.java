package course.robotics.eventing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventingApplication {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(EventingApplication.class, args);
		ctx.registerShutdownHook();
	}

}
