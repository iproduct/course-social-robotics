package org.iproduct.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class GetMqttKafkaVersion {
    private static final Logger log = LoggerFactory.getLogger(GetMqttKafkaVersion.class);
    private static String version = "unknown";

    static {
        try (InputStream in = GetMqttKafkaVersion.class.getResourceAsStream(
                "/mqtt-kafka-bringe.properties")) {
            Properties props = new Properties();
            props.load(in);
            version = props.getProperty("version", version).trim();
        } catch (Exception e) {
            log.warn("Error while loading version from 'kafka-connect-mqtt-version.properties':", e);
        }
    }

    public static String getVersion() {
        return version;
    }
}
