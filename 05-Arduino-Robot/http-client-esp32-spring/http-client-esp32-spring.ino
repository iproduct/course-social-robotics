/**
 * BasicHTTPClient.ino
 *
 *  Created on: 17.04.2019
 *
 */

#include <WiFi.h>
#include <HTTPClient.h>
#define USE_SERIAL Serial
#include "arduino_secrets.h"

const char* ssid = SECRET_SSID;
const char* pass = SECRET_PASS;
int distance = 5;
char apiUrl[] = "http://192.168.0.12:8080/api/events";    // your Events API URL

void setup() {
 //Initialize serial and wait for port to open:
  Serial.begin(115200);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // attempt to connect to Wifi network:
  int status = WL_IDLE_STATUS;
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);

    // wait 3 seconds for connection:
    delay(3000);
  }

  Serial.println("Connected to wifi");
  printWifiStatus();
}

void loop() {
  // wait for WiFi connection
  HTTPClient http;

  USE_SERIAL.print("[HTTP] begin...\n");
  // configure traged server and url
  //http.begin("https://www.howsmyssl.com/a/check", ca); //HTTPS
  http.begin(apiUrl); //HTTP

  USE_SERIAL.print("[HTTP] Post: ");
  USE_SERIAL.println(apiUrl);
  // start connection and send HTTP header
//    int httpCode = http.GET();
  http.addHeader("Content-Type", "application/json");             //Specify content-type header

  char eventString[256]; //= "{\"time\":1, \"distance\":5}"; 
  sprintf(eventString, "{\"time\":%d, \"distance\":%d}", millis(), distance++);
  Serial.println(eventString);
  int httpResponseCode = http.POST(eventString);   //Send the actual POST request
 
  if(httpResponseCode>0){
    String response = http.getString();                       //Get the response to the request
    Serial.println(httpResponseCode);   //Print return code
    Serial.println(response);           //Print request answer
 
  }else{
    Serial.print("Error on sending POST: ");
    Serial.println(httpResponseCode);
  }
   
  http.end();

  delay(5000);
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
