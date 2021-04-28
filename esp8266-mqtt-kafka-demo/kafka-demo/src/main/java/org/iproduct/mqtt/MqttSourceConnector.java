package org.iproduct.mqtt;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a Kafka SourceConnector generating tasks to ingest MQTT messages.
 **/
public class MqttSourceConnector extends SourceConnector {
    private static final Logger log = LoggerFactory.getLogger(MqttSourceConnector.class);

    private Map<String, String> configProps;
    private MqttSourceConnectorConfig config;

    /**
     * Get the version of this Kafka connector class.
     *
     * @return the version String
     */
    @Override
    public String version() {
        return GetMqttKafkaVersion.getVersion();
    }

    /**
     * Starting the connector.
     *
     * @param props configuration settings
     */
    @Override
    public void start(Map<String, String> props) {
        log.info("Start a MqttSourceConnector with props:" + props);
        configProps = props;
        config = new MqttSourceConnectorConfig(configProps);
    }

    /**
     * SourceTask for this connector.
     *
     * @return SourceTask class
     */
    @Override
    public Class<? extends Task> taskClass() {
        return MqttSourceTask.class;
    }

    /**
     * Returns a set of configurations for SourceTasks.
     *
     * @param maxTasks maximum number of generated configurations
     *
     * @return Task configurations
     */
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>(1);
        Map<String, String> taskProps = new HashMap<>(configProps);
        taskConfigs.add(taskProps);
        return taskConfigs;
    }

    /**
     * Stopping the connector.
     */
    @Override
    public void stop() {
        log.info("Stopping the MqttSourceConnector");
    }

    @Override
    public ConfigDef config() {
        return MqttSourceConnectorConfig.config;
    }
}
