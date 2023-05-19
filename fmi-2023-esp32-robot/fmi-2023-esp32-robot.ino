#include <WiFi.h>
#include <WiFiUdp.h>
#include <coap-simple.h>

const char *ssid = "robots";
const char *password = "robot123";
const int LED = 15;

IPAddress remote_ip(192, 168, 1, 100);
const int remote_port = 5683;

// CoAP client response callback
void callback_response(CoapPacket &packet, IPAddress ip, int port);

// CoAP server endpoint url callback
void callback_light(CoapPacket &packet, IPAddress ip, int port);

typedef struct
{
  float reading[2];
} USReadings;

// UDP and CoAP class
// other initialize is "Coap coap(Udp, 512);"
// 2nd default parameter is COAP_BUF_MAX_SIZE(defaulit:128)
// For UDP fragmentation, it is good to set the maximum under
// 1280byte when using the internet connection.
WiFiUDP udp;
Coap coap(udp);

// LED STATE
bool LEDSTATE;

char eventString[256];

// CoAP server endpoint URL
void callback_light(CoapPacket &packet, IPAddress ip, int port) {
  Serial.println("[Light] ON/OFF");

  // send response
  char p[packet.payloadlen + 1];
  memcpy(p, packet.payload, packet.payloadlen);
  p[packet.payloadlen] = NULL;

  char t[packet.tokenlen + 1];
  memcpy(t, packet.token, packet.tokenlen);

  String message(p);
  String token(t);

  if (message.equals("0"))
    LEDSTATE = false;
  else if (message.equals("1"))
    LEDSTATE = true;

  if (LEDSTATE) {
    digitalWrite(LED, HIGH);
    coap.sendResponse(ip, port, packet.messageid, "1", strlen("1"), COAP_CONTENT, COAP_TEXT_PLAIN, reinterpret_cast<const uint8_t *>(&token[0]), packet.tokenlen);
  } else {
    digitalWrite(LED, LOW);
    coap.sendResponse(ip, port, packet.messageid, "0", strlen("0"), COAP_CONTENT, COAP_TEXT_PLAIN, reinterpret_cast<const uint8_t *>(&token[0]), packet.tokenlen);
  }
  Serial.println(message);
  Serial.println(token);
  Serial.println(ip);
  Serial.println(port);
}

// CoAP client response callback
void callback_response(CoapPacket &packet, IPAddress ip, int port) {
  Serial.println("[Coap Response got]");

  char p[packet.payloadlen + 1];
  memcpy(p, packet.payload, packet.payloadlen);
  p[packet.payloadlen] = NULL;

  Serial.println(p);
}

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

void setup() {
  Serial.begin(115200);
  setupUSSensors();  // setup ultrasound sensors

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  printWifiStatus();

  // LED State
  pinMode(LED, OUTPUT);
  digitalWrite(LED, HIGH);
  LEDSTATE = true;

  // add server url endpoints.
  // can add multiple endpoint urls.
  // exp) coap.server(callback_switch, "switch");
  //      coap.server(callback_env, "env/temp");
  //      coap.server(callback_env, "env/humidity");
  Serial.println("Setup Callback Light");
  coap.server(callback_light, "light");

  // client response callback.
  // this endpoint is single callback.
  Serial.println("Setup Response Callback");
  coap.response(callback_response);

  // start coap server/client
  coap.start();

  coap.get(remote_ip, 5683, "time");
}

void loop() {
  coap.loop();
  // delay(1000);

  USReadings readings = readUSDistance();
  sprintf(eventString, "{\"type\":\"distance\", \"time\":%d, \"distanceL\":%f, \"distanceR\":%f}", millis(), readings.reading[0], readings.reading[1]);
  Serial.println(eventString);
  coap.put(remote_ip, 5683, "sensors/distance", eventString, strlen(eventString));
}
/*
if you change LED, req/res test with coap-client(libcoap), run following.
coap-client -m get coap://(arduino ip addr)/light
coap-client -e "1" -m put coap://(arduino ip addr)/light
coap-client -e "0" -m put coap://(arduino ip addr)/light
*/
