package org.iproduct.demos.spring.streamingdemos.handlers;

import org.iproduct.demos.spring.streamingdemos.udp.UDPServer;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class ReactiveIotEventsHandler {

    @Autowired
    private UDPServer server;


    public Mono<ServerResponse> streamEvents(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(APPLICATION_STREAM_JSON)
            .body(
                server.getEventEmitter().map(payload -> {
                            String[] tokens = payload.split("\\s+");
                            JSONObject obj = new JSONObject();
                            obj.put("time", Long.parseLong(tokens[1]));
                            obj.put("button", Integer.parseInt(tokens[21]));
                            obj.put("distance", Integer.parseInt(tokens[23]));

                            obj.put("roll", Float.parseFloat(tokens[3]));
                            obj.put("pitch", Float.parseFloat(tokens[5]));
                            obj.put("yaw", Float.parseFloat(tokens[7]));

                            JSONObject accel = new JSONObject();
                            accel.put("x", Integer.parseInt(tokens[9]));
                            accel.put("y", Integer.parseInt(tokens[10]));
                            accel.put("z", Integer.parseInt(tokens[11]));
                            obj.put("accel", accel);

                            JSONObject gyro = new JSONObject();
                            gyro.put("x", Integer.parseInt(tokens[13]));
                            gyro.put("y", Integer.parseInt(tokens[14]));
                            gyro.put("z", Integer.parseInt(tokens[15]));
                            obj.put("compass", gyro);

                            JSONObject compass = new JSONObject();
                            compass.put("x", Integer.parseInt(tokens[17]));
                            compass.put("y", Integer.parseInt(tokens[18]));
                            compass.put("z", Integer.parseInt(tokens[19]));
                            obj.put("gyro", compass);

                            return obj.toJSONString();
                        })
                , String.class);

    }

    public Mono<ServerResponse> streamEventsSSE(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(server.getEventEmitter(), String.class);
    }

    public Mono<ServerResponse> streamEventsJsonSSE(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(
                        server.getEventEmitter().map(payload -> {
                            String[] tokens = payload.split("\\s+");
                            JSONObject obj = new JSONObject();
                            obj.put("time", Long.parseLong(tokens[1]));
                            obj.put("button", Integer.parseInt(tokens[21]));
                            obj.put("distance", Integer.parseInt(tokens[23]));

                            obj.put("roll", Float.parseFloat(tokens[3]));
                            obj.put("pitch", Float.parseFloat(tokens[5]));
                            obj.put("yaw", Float.parseFloat(tokens[7]));

                            JSONObject accel = new JSONObject();
                            accel.put("x", Integer.parseInt(tokens[9]));
                            accel.put("y", Integer.parseInt(tokens[10]));
                            accel.put("z", Integer.parseInt(tokens[11]));
                            obj.put("accel", accel);

                            JSONObject gyro = new JSONObject();
                            gyro.put("x", Integer.parseInt(tokens[13]));
                            gyro.put("y", Integer.parseInt(tokens[14]));
                            gyro.put("z", Integer.parseInt(tokens[15]));
                            obj.put("compass", gyro);

                            JSONObject compass = new JSONObject();
                            compass.put("x", Integer.parseInt(tokens[17]));
                            compass.put("y", Integer.parseInt(tokens[18]));
                            compass.put("z", Integer.parseInt(tokens[19]));
                            obj.put("gyro", compass);

                            return obj.toJSONString();
                        })
                        , String.class);
    }

}
