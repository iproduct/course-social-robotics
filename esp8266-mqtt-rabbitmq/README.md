# Course Internet of Things (IoT)
Internet of Things (IoT) demos and examples with ESP8266 WiFi module 

## Arduino ESP8266 MQTT Client Demo
Demo shows how to implement bidirectional communication using MQTT protocol using ESP8266 WiFi module.

In order to run the demo you have to have running Messaaging Queus (MQ) Server.
You have to provide following settings to run the demo:

* ssid = "Your WiFi SSID";
* password = "Your WiFi password";
* mqtt_server = "Your MQTT Server IP address";
* mqtt_user = "Your MQTT Server username";
* mqtt_password = "Your MQTT Server password";

If you use anonimous MQTT connection (if you server allows it), you can skip specifing MQQT Server username and password, and you can change line 100 in the program to:
``` 
if (client.connect("ESP8266Client")) { 
    ...
```

## Installing Configuring and Running RabbitMQ Server
RabbitMQ is one of most widely used MQ server implementations. It is written in Erlang and provides outstanding performance and scalability as well as rich functionality on  a modular basis. In order to run it with the ESP8266 client you have to install it, enable MQTT module, and create a user for the demo.

### Installing RabbitMQ
1. JDK 8 - http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. OTP 20.3 full - http://www.erlang.org/downloads
3. RabbitMQ - http://www.rabbitmq.com/download.html
4. (Optional) NodeJS 9.x.x (latest)  - https://nodejs.org/en/download/current/

### Configuring RabbitMQ
1. Open RabbitMQ console (shortcut is created in the start menu during installation).
2. Enable MQTT following  modules:
```
[e ] rabbitmq_management_agent         3.7.4
[E ] rabbitmq_mqtt                     3.7.4
[e ] rabbitmq_web_dispatch             3.7.4
[E ] rabbitmq_web_mqtt                 3.7.4
[E ] rabbitmq_web_mqtt_examples        3.7.4
```
by running following command from RabbitMQ console:
```
rabbitmq-plugins enable rabbitmq_management_agent rabbitmq_mqtt rabbitmq_web_mqtt rabbitmq_web_mqtt_examples
```
you can check the plugins are enabled by running:
```
rabbitmq-plugins list
```

4. Run the RabbitMQ server (<install-dir>/sbin/rabbitmq-serverbat):
```
rabbitmq-server
```

3. Open web coonole at: http://localhost:15672/
user: guest, password: guest

4. Create a new Exchange - go to Exchanges / Add a new exchange - e.g. name: test-exchange, type: topic, durability: durable

5. Create a new user from web console - go to Admin / Add User, and fill in new user data - e.g. username: test, password: test, tags: Management

6. Click on the user and set all permissions (.*) for configuring, reding, and writing for host "/". Set all (.*)  topic permissions for exchange "test-exchange " we have created. Alternatively you could create user from command line:
```
rabbitmqctl add_user test test
rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
rabbitmqctl set_user_tags test management
```

7. Goto user config dir (e.g. in C:\Users\user123\AppData\Roaming\RabbitMQ), and rename defult config file from ```rabbitmq.config``` to ```rabbitmq.config.old```.
Create/modify RabbitMQ configuation file ```rabbitmq.conf``` and add following config settings:
```
mqtt.default_user     = test
mqtt.default_pass     = test
mqtt.allow_anonymous  = true
mqtt.vhost            = /
mqtt.exchange         = test-topic
# 24 hours by default
mqtt.subscription_ttl = 86400000
mqtt.prefetch         = 10
mqtt.listeners.ssl    = none
## Default MQTT with TLS port is 8883
# mqtt.listeners.ssl.default = 8883
mqtt.listeners.tcp.default = 1883
mqtt.tcp_listen_options.backlog = 128
mqtt.tcp_listen_options.nodelay = true
```

## Running RabbitMQ Java Client
In order to run java MQTT messages Recv (receive) and Send demos you can first import the Maven-based project in your favourite IDE (IntelliJ, Eclipse, etc.), and then run corresponding Java client programs:
* Recv.java - prints received messages to the console (configured with the example RabbitMQ topic exchange createed in previos section)
* Send.java - sends 100 random command messages to ESP8266 board - "0" means LED off, "1" means LED on :)

Enjoi the demo, and feel free to contact me at: tiliev @ iproduct.org :)


