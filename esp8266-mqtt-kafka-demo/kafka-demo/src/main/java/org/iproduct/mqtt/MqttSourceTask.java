package org.iproduct.mqtt;

import com.rabbitmq.client.*;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.iproduct.mqtt.MqttSourceConstants.*;

/**
 * MqttSourceTask is a Kafka Connect SourceTask implementation that reads
 * from MQTT and generates Kafka Connect records.
 */
public class MqttSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(MqttSourceTask.class);

    private Connection connection;
    private Channel channel;
    private MqttSourceConnectorConfig config;

    BlockingQueue<String> mQueue = new LinkedBlockingQueue<String>();

    @Override
    public String version() {
        return GetMqttKafkaVersion.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        log.info("Start a MqttSourceTask");
        config = new MqttSourceConnectorConfig(props);
        System.out.println("CURRENT CONFIG: " + config.values());

        // Setup topic
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getString(MQTT_SERVER_HOST));
            factory.setPort(config.getInt(MQTT_SERVER_PORT));

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(config.getString(MQTT_TOPIC), "topic", true );
            String queueName = channel.queueDeclare(config.getString(MQTT_QUEUE_NAME), false, false, false, null).getQueue();
            channel.queueBind(queueName, config.getString(MQTT_TOPIC), config.getString(MQTT_BINDING_KEY));
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    log.debug("[{}] New message on arrived: '{}'.", channel.getChannelNumber(), message);

                    mQueue.add(message);
                }
            };
            channel.basicConsume(config.getString(MQTT_QUEUE_NAME), true, consumer);

            log.info("[{}] Subscribe to '{}' with Channel '{}'", consumer, config.getString(MQTT_TOPIC),
                    channel.getChannelNumber());
        } catch (Exception e) {
            log.error("Subscribe failed! ", e);
        }
    }

    @Override
    public void stop() {
        log.info("Stoping the MqttSourceTask");

        try {
            connection.close();
            log.info("Disconnected from Broker.");
        } catch (Exception e) {
            log.error("Disconnecting from Broker failed!", e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<>();
        String message = mQueue.take();
        log.debug("[{}] Polling new data from queue for '{}' topic.",
                channel.getChannelNumber(), config.getString(KAFKA_TOPIC));

        SourceRecord[] sourceRecords = new SourceRecord[]{new SourceRecord(null, null, config.getString(KAFKA_TOPIC), null,
                Schema.STRING_SCHEMA, config.getString(MQTT_TOPIC),
                Schema.STRING_SCHEMA, message)};

        Collections.addAll(records, sourceRecords);

        return records;
    }

}
