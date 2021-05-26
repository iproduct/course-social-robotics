package course.robotics.eventing.udp;

public interface EventListener<T> {
    void onEvent(T event);
}
