/**
 * BasicHTTPClient.ino
 *
 *  Created on: 17.04.2019
 *
 */

#include <WiFi.h>
#include <HTTPClient.h>
#include "arduino_secrets.h"

const char* ssid = SECRET_SSID;
const char* pass = SECRET_PASS;
const char* apiUrl = "http://192.168.0.12:8080/api/events";    // your Events API URL

const int trigPin = 15;
const int echoPin = 2;
const int LED = 23;

HTTPClient http;

void setup() {
 //Initialize serial and wait for port to open:
  Serial.begin(115200);
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
  pinMode(LED, OUTPUT);     // set the LED pin mode
}

void loop() {
  digitalWrite(LED, HIGH);  
  int distance = readDistance();
  Serial.print("\nDistance is: ");  //output result to Serial monitor
  Serial.print(distance, DEC);            //output result to Serial monitor
  Serial.println("cm"); 

  sendReadingPOST(distance);
  digitalWrite(LED, LOW);  
  
  delay(5000);
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

String sendReadingPOST(int distance) {
  Serial.print("[HTTP] begin...\n");
  http.begin(apiUrl); //HTTP

  Serial.print("[HTTP] Post: ");
  Serial.println(apiUrl);
  http.addHeader("Content-Type", "application/json");             //Specify content-type header
  char eventString[256];
  sprintf(eventString, "{\"time\":%d, \"distance\":%d}", millis(), distance);
  Serial.println(eventString);
  int httpResponseCode = http.POST(eventString);   //Send the actual POST request
 
  if(httpResponseCode>0){
    String response = http.getString();                       //Get the response to the request
    Serial.println(httpResponseCode);   //Print return code
    Serial.println(response);           //Print request answer
    return response;
  }else{
    Serial.print("Error on sending POST: ");
    Serial.println(httpResponseCode);
    return "";
  }
  http.end();
}

int readDistance() {
  // Read US sensor data
  digitalWrite(trigPin, HIGH);              //begin to send a high pulse, then US-015 begin to measure the distance
  delayMicroseconds(50);                    //set this high pulse width as 50us (>10us)
  digitalWrite(trigPin, LOW);               //end this high pulse  delayMicroseconds(10);
  
  unsigned long duration = pulseIn(echoPin, HIGH);
//  if((duration < 60000) && (duration > 1)) {
    return duration;
//  } else {
//    return -1;
//  }
}
