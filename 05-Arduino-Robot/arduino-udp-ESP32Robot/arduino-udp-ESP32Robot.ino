#include <AsyncUDP.h>
#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>
#include "arduino_secrets.h"


// WiFi connection data
const char* ssid = SECRET_SSID;
const char* pass = SECRET_PASS;
const IPAddress udp_server_ip = IPAddress(10, 108, 7, 160); //"10.108.7.160"; //"192.168.0.12";
unsigned int udp_server_port = 4210;

//WiFiUDP Udp;
AsyncUDP udp;
unsigned int localUdpPort = 4210;  // local port to listen on
char incomingPacket[255];  // buffer for incoming packets

//Ultrasound sensor SNS-US020
const int trigPin = 23;
const int echoPin = 22;
int distance = -1;
char report[256];
long timer;
long timer_old;
long counter;


void setup()
{
  //Initialize serial and wait for port to open:
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // attempt to connect to Wifi network:
  Serial.println();
  Serial.println();
  Serial.print("Connecting to: ");
  Serial.println(ssid);

  WiFi.begin(ssid, pass);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  printWifiStatus();

  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  //  udp.begin(localUdpPort);
  if (udp.connect(udp_server_ip, udp_server_port)) {
    Serial.println("UDP connected");
    udp.onPacket([](AsyncUDPPacket packet) {
      Serial.print("UDP Packet Type: ");
      Serial.print(packet.isBroadcast() ? "Broadcast" : packet.isMulticast() ? "Multicast" : "Unicast");
      Serial.print(", From: ");
      Serial.print(packet.remoteIP());
      Serial.print(":");
      Serial.print(packet.remotePort());
      Serial.print(", To: ");
      Serial.print(packet.localIP());
      Serial.print(":");
      Serial.print(packet.localPort());
      Serial.print(", Length: ");
      Serial.print(packet.length());
//      String data = String((char *)packet.data());
      int len = snprintf(report, packet.length(), "%s", packet.data());
      char* data = report + 1;
//      String data = (char*)packet.data();
//      String data = String(report).substring(1,packet.length());
      Serial.print(", Data: ");
      Serial.print(data);
      Serial.println();
      //reply to the client
      counter++;
      packet.printf("{\"id\":%d, \"time\":%d, \"command\": \"%s\", \"complete\": true}",
                       counter,
                       timer,
                       data);
    });
  }
  
  Serial.printf("Now listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), localUdpPort);
  timer = millis();
  delay(20);
  counter = 0;
  sendMessageUdp("connect 1");
}


void loop()
{
  //  int packetSize = Udp.parsePacket();
  //  if (packetSize)
  //  {
  //    // receive incoming UDP packets
  //    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
  //    int len = Udp.read(incomingPacket, 255);
  //    if (len > 0)
  //    {
  //      incomingPacket[len] = 0;
  //    }
  //    Serial.printf("UDP packet contents: %s\n", incomingPacket);
  //  compass.read();
  //  gyro.read();
  //snprintf(report, sizeof(report), "T:%12d   A: %6d %6d %6d   M: %6d %6d %6d   G: %6d %6d %6d   B: %1d   D: %5d",
  //    now,
  //    compass.a.x, compass.a.y, compass.a.z,
  //    compass.m.x, compass.m.y, compass.m.z,
  //    gyro.g.x, gyro.g.y, gyro.g.z,
  //    buttonState, distance);

  if ((millis() - timer) >= 1000) // Main loop runs at 1Hz
  {
    counter++;
    timer_old = timer;
    timer = millis();

    // US-020 - read US sensor data
    float distance = readDistance();
    int len = snprintf(report, sizeof(report), "{\"id\":%d, \"time\":%d, \"USL\": %5.2f}",
                       counter,
                       timer,
                       distance);
    Serial.println(report);
    sendMessageUdp(report);
  }
}
void sendMessageUdp(const char* message) {
  // send back a reply, to the IP address and port we got the packet from
  //  int result = Udp.beginPacket(udp_server_ip, udp_server_port);
  //  if (result > 0) {
//  udp.write((const uint8_t*)message, strlen(message));
  udp.print(message);
  //    Udp.endPacket();
  //  }
}


// utility functions
void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

float readDistance() {
  // Read US sensor data
  digitalWrite(trigPin, HIGH);              //begin to send a high pulse, then US-015 begin to measure the distance
  delayMicroseconds(10);                    //set this high pulse width as 50us (>10us)
  digitalWrite(trigPin, LOW);               //end this high pulse  delayMicroseconds(10);

  unsigned long duration = pulseIn(echoPin, HIGH);
  if ((duration < 60000) && (duration > 1)) {
    return duration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back);
  } else {
    return -1;
  }
}
