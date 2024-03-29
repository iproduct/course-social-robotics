#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <ESPmDNS.h>
#include "arduino_secrets.h"
#define potPin 36
#define ledPin 12

WebServer server(80);

int potVal;

void handleRoot() {
  server.send(200, "text/html",
              "<!DOCTYPE html>\
  <html>\
  <body>\
  <h2>Arduino ESP32 Web Server &#9729;</h2>\
  </body>\
  </html>");
}

void handleNotFound() {
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
}

void handleLED() {
  String message = "";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  if (server.arg("state") == "on") {
    digitalWrite(ledPin, 1);
    Serial.println("LED ON");
  } else {
    digitalWrite(ledPin, 0);
    Serial.println("LED OFF");
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
  pinMode(ledPin, OUTPUT);
  // pinMode(potPin, INPUT);

  digitalWrite(ledPin, 0);

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

  potVal = analogRead(potPin);
  Serial.print("potVal: ");
  Serial.println(potVal);

  delay(500);
}
