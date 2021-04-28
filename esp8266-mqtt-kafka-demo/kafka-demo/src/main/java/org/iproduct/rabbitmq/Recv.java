package org.iproduct.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

  private final static String EXCHANGE_NAME = "test-topic";
  private final static String QUEUE_NAME = "test-queue";
  private final static String BINDING_KEY = "outTopic";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true );
    String queueName = channel.queueDeclare(QUEUE_NAME, false, false, false, null).getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, BINDING_KEY);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
