/*
 Basic ESP8266 MQTT example

 This sketch demonstrates the capabilities of the pubsub library in combination
 with the ESP8266 board/library.

 It connects to an MQTT server then:
  - publishes "hello world" to the topic "outTopic" every two seconds
  - subscribes to the topic "inTopic", printing out any messages
    it receives. NB - it assumes the received payloads are strings not binary
  - If the first character of the topic "inTopic" is an 1, switch ON the ESP Led,
    else switch it off

 It will reconnect to the server if the connection is lost using a blocking
 reconnect function. See the 'mqtt_reconnect_nonblocking' example for how to
 achieve the same result without blocking the main loop.

 To install the ESP8266 board, (using Arduino 1.6.4+):
  - Add the following 3rd party board manager under "File -> Preferences -> Additional Boards Manager URLs":
       http://arduino.esp8266.com/stable/package_esp8266com_index.json
  - Open the "Tools -> Board -> Board Manager" and click install for the ESP8266"
  - Select your ESP8266 in "Tools -> Board"

*/

#include <ESP8266WiFi.h>
#include <PubSubClient.h>

// Update these with values suitable for your network.

const char* ssid = "your-wifi-network-ssid";
const char* password = "your-wifi-network-password";
const char* mqtt_server = "192.168.137.1";
const char* mqtt_user = "test";
const char* mqtt_password = "test";
const char* mqtt_out_routing_key = "DISTANCE";
const char* mqtt_in_routing_key = "COMMANDS";

const int  buttonPin = D6;    // the pin that the pushbutton is attached to

// Variables will change:
int buttonPushCounter = 0;   // counter for the number of button presses
int buttonState = 0;         // current state of the button
int lastButtonState = 0;     // previous state of the button

//Ultrasound sensor SNS-US020
unsigned int EchoPin = D5;           // connect Pin 4(Arduino digital io) to Echo at US-015
unsigned int TrigPin = D3;           // connect Pin 3(Arduino digital io) to Trig at US-015
unsigned long Time_Echo_us = 0;
int len_cm  = 0;


WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;

void setup() {
  pinMode(buttonPin, INPUT_PULLUP);        // initialize the button pin as a input
  pinMode(BUILTIN_LED, OUTPUT);     // Initialize the BUILTIN_LED pin as an output

  //Initialize ultrasound sensor
  pinMode(EchoPin, INPUT);                    //Set EchoPin as input, to receive measure result from US-015
  pinMode(TrigPin, OUTPUT);                   //Set TrigPin as output, used to send high pusle to trig measurement (>10us)
    
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void setup_wifi() {

  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();

  // Switch on the LED if an 1 was received as first character
  if ((char)payload[0] == '1') {
    digitalWrite(BUILTIN_LED, LOW);   // Turn the LED on (Note that LOW is the voltage level
    // but actually the LED is on; this is because
    // it is acive low on the ESP-01)
  } else {
    digitalWrite(BUILTIN_LED, HIGH);  // Turn the LED off by making the voltage HIGH
  }

}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client", mqtt_user, mqtt_password)) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish(mqtt_out_routing_key, "init");
      // ... and resubscribe
      client.subscribe(mqtt_in_routing_key);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
void loop() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  long now = millis();
  if (now - lastMsg > 2000) {
    // read the pushbutton input pin:
    buttonState = digitalRead(buttonPin);
    Serial.println(buttonState);
    if (buttonState == LOW) {
        buttonPushCounter++;
        snprintf (msg, 50, "on");
    } else {
        snprintf (msg, 50, "off");
    }
    lastMsg = now;
    Serial.print("Publish message: ");
    Serial.println(msg);
    client.publish(mqtt_out_routing_key, msg);

    // Read US sensor data
    digitalWrite(TrigPin, HIGH);              //begin to send a high pulse, then US-015 begin to measure the distance
    delayMicroseconds(50);                    //set this high pulse width as 50us (>10us)
    digitalWrite(TrigPin, LOW);               //end this high pulse
    
    Time_Echo_us = pulseIn(EchoPin, HIGH);               //calculate the pulse width at EchoPin, 
    if((Time_Echo_us < 60000) && (Time_Echo_us > 1))     //a valid pulse width should be between (1, 60000).
    {
      len_cm = (Time_Echo_us*34/100)/20;      //calculate the distance by pulse width, Len_mm = (Time_Echo_us * 0.34mm/us) / 20 (cm)
      Serial.print("Present Distance is: ");  //output result to Serial monitor
      Serial.print(len_cm, DEC);            //output result to Serial monitor
      Serial.println("cm");                 //output result to Serial monitor
      snprintf (msg, 50, "%ld", len_cm);
      client.publish(mqtt_out_routing_key, msg);      // publish to MQTT server
    }
  }
  
}
