import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class Send {

  private final static String EXCHANGE_NAME = "test-topic";
  private final static String ROUTING_KEY = "inTopic";
  private final static Random rand = new Random();

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

    for(int i = 0; i < 100; i++) {
      String message = rand.nextInt(2) + "";
      channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
      System.out.println(" [x] Sent '" + message + "'");
      Thread.sleep(3000);
    }

    channel.close();
    connection.close();
  }
}
