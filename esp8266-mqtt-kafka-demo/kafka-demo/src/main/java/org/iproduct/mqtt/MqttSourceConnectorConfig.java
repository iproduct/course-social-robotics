/**
 * Copyright 2016 Evokly S.A.
 * See LICENSE file for License
 **/

package org.iproduct.mqtt;

//import com.evokly.kafka.connect.mqtt.sample.DumbProcessor;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MqttSourceConnectorConfig is responsible for correct configuration management.
 */
public class MqttSourceConnectorConfig extends AbstractConfig {
    private static final Logger log = LoggerFactory.getLogger(MqttSourceConnector.class);

    /**
     * Create default mConfig.
     * @return default mConfig
     */
    public static ConfigDef baseConfigDef() {
        return new ConfigDef()
                .define(MqttSourceConstants.KAFKA_TOPIC, Type.STRING, "streams-plaintext-input", Importance.HIGH,
                        "Kafka topic to put received data \n Depends on message processor")
                .define(MqttSourceConstants.MQTT_SERVER_HOST, Type.STRING, "localhost", Importance.HIGH,
                        "mqtt server host to connect to")
                .define(MqttSourceConstants.MQTT_SERVER_PORT, Type.INT, 5672, Importance.LOW,
                        "mqtt server port to connect to")
                .define(MqttSourceConstants.MQTT_TOPIC, Type.STRING, "test-topic", Importance.HIGH,
                        "mqtt server topic to connect to") // RabbitMQ exchange name
                .define(MqttSourceConstants.MQTT_QUEUE_NAME, Type.STRING, "test-queue", Importance.HIGH,
                        "mqtt topic queue to connect to")
                .define(MqttSourceConstants.MQTT_BINDING_KEY, Type.STRING, "test-queue", Importance.HIGH,
                        "mqtt queue bindin key")
                .define(MqttSourceConstants.MQTT_CONNECTION_TIMEOUT, Type.INT, 30, Importance.LOW,
                        "connection timeout to use")
                .define(MqttSourceConstants.MQTT_KEEP_ALIVE_INTERVAL, Type.INT, 60, Importance.LOW,
                        "keepalive interval to use")

                .define(MqttSourceConstants.MQTT_QUALITY_OF_SERVICE, Type.INT, 1, Importance.LOW,
                        "mqtt qos to use")
                .define(MqttSourceConstants.MQTT_USERNAME, Type.STRING, null, Importance.MEDIUM,
                        "username to authenticate to mqtt broker")
                .define(MqttSourceConstants.MQTT_PASSWORD, Type.STRING, null, Importance.MEDIUM,
                        "password to authenticate to mqtt broker");
    }

    static ConfigDef config = baseConfigDef();

    /**
     * Transform process properties.
     *
     * @param properties associative array with properties to be process
     */
    public MqttSourceConnectorConfig(Map<String, String> properties) {
        super(config, properties);
        log.info("Initialize transform process properties");
    }

    public static void main(String[] args) {
        System.out.println(config.toRst());
    }
}
