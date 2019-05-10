#include "WiFi.h"
#include "AsyncUDP.h"
#include <Adafruit_NeoPixel.h>

// WiFi connection data
const char* ssid = "your-wifi-network-ssid";
const char* password = "your-wifi-network-ssid";
//const char* udp_server_ip = "192.168.137.1";
IPAddress udp_server_ip(192,168,137,1);
unsigned int udp_server_port = 4210;
unsigned int client_id = 1;

// Neopixel config data
#define  PIN 22
#define  NUMBER_OF_LEDS  5
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUMBER_OF_LEDS, PIN, NEO_RGB + NEO_KHZ800);

AsyncUDP udp;

char buffer[256];

void setup()
{
    Serial.begin(115200);

    // Neopixel leds
    strip.begin();
    strip.show(); // Initialize all pixels to 'off'

    // WiFi
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    if (WiFi.waitForConnectResult() != WL_CONNECTED) {
        Serial.println("WiFi Failed");
        while(1) {
            delay(1000);
        }
    }
    printWifiStatus();
    
    if(udp.connect(udp_server_ip, udp_server_port)) {
        Serial.println("UDP connected");
        udp.onPacket([](AsyncUDPPacket packet) {
            Serial.print("UDP Packet Type: ");
            Serial.print(packet.isBroadcast()?"Broadcast":packet.isMulticast()?"Multicast":"Unicast");
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
            Serial.print(", Data: ");
            Serial.write(packet.data(), packet.length());
            Serial.println();

            String commandStr = (char *)(packet.data());
            commandStr = commandStr.substring(0, packet.length());
            parseAndExecuteCommand(commandStr);
            
            //reply to the client
            packet.printf("Got %u bytes of data", packet.length());
        });
        //Send unicast
//        snprintf(buffer, sizeof(buffer), "%s,%d", WiFi.localIP().toString().c_str(), udp_server_port);
        snprintf(buffer, sizeof(buffer), "connect %d", client_id);
        Serial.println(buffer);
        udp.print(buffer);
    }
}

void loop()
{
    delay(1000);
    //Send broadcast on port udp_server_port
    udp.broadcastTo("Anyone here?", udp_server_port);
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

int stringSplit(String sInput, char cDelim, String sParams[], int iMaxParams)
{
    int iParamCount = 0;
    int iPosDelim, iPosStart = 0;

    do {
      // Searching the delimiter using indexOf()
      iPosDelim = sInput.indexOf(cDelim, iPosStart);
      if (iPosDelim > iPosStart) {
        // Adding a new parameter using substring() 
        sParams[iParamCount] = sInput.substring(iPosStart,iPosDelim);
        iParamCount++;
        // Checking the number of parameters
        if (iParamCount >= iMaxParams) {
            return (iParamCount);
        }
        iPosStart = iPosDelim + 1;
      } else if (iPosDelim > 0) {
        iPosStart++;
      }
    } while (iPosDelim >= 0);
    if (iParamCount < iMaxParams) {
      // Adding the last parameter as the end of the line
      sParams[iParamCount] = sInput.substring(iPosStart, sInput.length());
      iParamCount++;
    }
    return (iParamCount);
}

void parseAndExecuteCommand(String commandMsg) {
  String params[10];
  int pCount, i;
  if (commandMsg.length() > 0) {
    // parse the line
    pCount = stringSplit(commandMsg, ',', params, 10);
    // print the extracted paramters
    for(i=0; i < pCount; i++) {
        Serial.print(params[i]);
        Serial.print(", ");
    }
    Serial.println("");
    
    if( params[0].equals("ledIRGB") &&  pCount == 5) {
      Serial.println("Command: ledIRGB");
      strip.setPixelColor(params[1].toInt(), strip.Color(params[2].toInt(), params[3].toInt(), params[4].toInt()));
      strip.show();
    }
  }
}
