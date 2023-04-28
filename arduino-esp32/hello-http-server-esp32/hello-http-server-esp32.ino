#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <ESPmDNS.h>
#include "arduino_secrets.h"

const char* ssid = SECRET_SSID;
const char* password = SECRET_PASS;

WebServer server(80);

const int led = 15;

void handleRoot() {
  digitalWrite(led, 1);
  server.send(200, "text/html", 
  "<!DOCTYPE html>\
  <html>\
  <body>\
  <h2>Arduino ESP32 Web Server &#9729;</h2>\
  </body>\
  </html>");
  digitalWrite(led, 0);
}

void handleLED() {
  String message = "";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  if(server.arg("state") == "on") {
    digitalWrite(led, 1);
  } else {
    digitalWrite(led, 0);
  }
  server.send(200, "text/html", 
  "<!DOCTYPE html>\
  <html>\
  <body>\
  <h2>" + message + "</h2>\
  </body>\
  </html>");
  
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

void setup(void) {
  pinMode(led, OUTPUT);
  digitalWrite(led, 0);
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (MDNS.begin("esp32-trayan")) {
    Serial.println("MDNS responder started");
  }

  server.on("/", handleRoot);

  server.on("/led", handleLED);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });

  server.onNotFound(handleNotFound);

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void) {
  server.handleClient();
}
