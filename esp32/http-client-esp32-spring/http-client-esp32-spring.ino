/**
 * BasicHTTPClient.ino
 *
 *  Created on: 17.04.2019
 *
 */

#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#define USE_SERIAL Serial

char ssid[] = "FMI-AIR-NEW"; //  your network SSID (name)
char pass[] = "";    // your network password (use for WPA, or use as key for WEP)
char apiUrl[] = "http://10.108.6.106:8080/api/events";    // your Events API URL

void setup() {
  Serial.begin(115200);         // Start the Serial communication to send messages to the computer
  Serial.println('\n');
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
  printWifiStatus();
}

void loop() {
  // wait for WiFi connection
  HTTPClient http;

  USE_SERIAL.print("[HTTP] begin...\n");
  // configure traged server and url
  //http.begin("https://www.howsmyssl.com/a/check", ca); //HTTPS
  http.begin(apiUrl); //HTTP

  USE_SERIAL.print("[HTTP] GET: ");
  USE_SERIAL.println(apiUrl);
  // start connection and send HTTP header
//    int httpCode = http.GET();
  http.addHeader("Content-Type", "application/json");             //Specify content-type header

  char eventString[256]; 
  sprintf(eventString, "{time:\"%s\", distance:\"%s\"}", 1, 3);
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
