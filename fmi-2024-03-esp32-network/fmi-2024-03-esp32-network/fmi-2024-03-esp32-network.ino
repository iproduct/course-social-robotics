#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include "arduino_secrets.h"

WebServer server(80);

void handleRoot() {
  // digitalWrite(led, 1);
  server.send(200, "text/html",
              "<!DOCTYPE html>\
  <html>\
  <body>\
  <h2>Arduino ESP32 Web Server &#9729;</h2>\
  </body>\
  </html>");
  digitalWrite(led, 0);
}



void setup() {
  Serial.begin(115200);

  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, PASSWORD);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(SSID);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");

  server.on("/", handleRoot);
  server.begin();

  Serial.println("Setup done");
}

void loop() {
  server.handleClient();
}
