package org.iproduct.spring.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("destinations")
public class DestinationsConfig {


    private Map<String, DestinationInfo> queues = new HashMap<>();

    private Map<String, DestinationInfo> topics = new HashMap<>();


    public Map<String, DestinationInfo> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, DestinationInfo> queues) {
        this.queues = queues;
    }

    public Map<String, DestinationInfo> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, DestinationInfo> topics) {
        this.topics = topics;
    }

    // DestinationInfo stores the Exchange name and routing key used
    // by our producers when posting messages
    public static class DestinationInfo {

        private String exchange;
        private String routingKey;
        private boolean durableQueue;


        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }

        public boolean isDurableQueue() { return durableQueue; }

        public void setDurableQueue(boolean durableQueue) { this.durableQueue = durableQueue; }
    }

}