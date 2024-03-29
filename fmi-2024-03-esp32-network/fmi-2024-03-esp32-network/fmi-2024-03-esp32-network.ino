#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <ESPmDNS.h>
#include "arduino_secrets.h"

const int led = 12;
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
  // digitalWrite(led, 0);
}

void handleNotFound() {
  digitalWrite(led, 1);
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
  digitalWrite(led, 0);
}

void handleLED() {
  String message = "";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  if (server.arg("state") == "on") {
    digitalWrite(led, 1);
  } else {
    digitalWrite(led, 0);
  }
  server.send(200, "text/html",
              "<!DOCTYPE html>\
  <html>\
  <body>\
  <h2>" + message
                + "</h2>\
  </body>\
  </html>");
}

void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led, 0);

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

  if (MDNS.begin("esp32trayan")) {
    Serial.println("MDNS responder started");
  }
  server.on("/", handleRoot);

  server.on("/led", handleLED);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });

  server.onNotFound(handleNotFound);
  
  server.begin();

  Serial.println("Setup done");
}

void loop() {
  server.handleClient();
}
