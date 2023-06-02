#include <Arduino.h>
// #include <analogWrite.h>
#include <ESP32Servo.h>
#include <WiFi.h>
#include <WiFiUdp.h>
#include <coap-simple.h>
#include <ArduinoJson.h>

#define US_SERVO_INDEX 2          //index of the ultrasound sensor moved by 2 servos in US sensor array
#define HORIZONTAL_SERVO_INDEX 0  //index of the ultrasound sensor moved by 2 servos in US sensor array
#define VERTICAL_SERVO_INDEX 1    //index of the ultrasound sensor moved by 2 servos in US sensor array

#define BUTTON 12  // button pin

#define MAX_JSON_SIZE 1024      // maximum size of a JSON command message
#define MAX_COMMANDS 10         // maximum number of commands sent to the robot
#define DISTANCE_PER_PULSE 5.6  // traveled distance per encoder pulse

#define NO_READINGS 0
#define ULTRASOUND_DISTANCE 1  // ultrasound sensor readings
#define IR_DISTANCE 2          // no robot command received
#define ENCODER 3              // no robot command received

#define NO_COMMAND 0      // no robot command received
#define MOVE_FORWARD 1    // move robot forward command
#define MOVE_BACKWARD 2   // move robot forward command
#define TURN_LEFT 3       // turn robot left command
#define TURN_RIGHT 4      //  turn robot right command
#define STOP 5            // stop robot movement command
#define SWEEP_DISTANCE 6  //  measure disyances to objects command

const char *ssid = "robots";
const char *password = "robot123";

IPAddress remote_ip(192, 168, 1, 100);  // backend FastAPI python server IP
const int remote_port = 5683;

typedef struct
{
  int type;
  float readings[2];
} Readings;

typedef struct
{
  int id;
  int params[4];
} Command;

// CoAP client response callback
void callback_response(CoapPacket &packet, IPAddress ip, int port);

// CoAP server endpoint url callback
void callback_light(CoapPacket &packet, IPAddress ip, int port);

// setup ultrasound sensors and servos
void setupUSSensorsServos();

// read ultrasound distance from sensor with given index
float readUSDistance(int sensorIndex);

// move servo with given index to given angle
void attachServo(int index, int initialAngle);
void detachServo(int index);
void moveServo(int index, int angle);

// setup motor encoders
void setupEncoders();

// motor control functions
void trunMotorsOff();
void moveForward(int speed);
void moveBackward(int speed);
void turnLeftInPlace(int speed);
void turnRightInPlace(int speed);
Readings updateSpeed();

// UDP and CoAP class
// other initialize is "Coap coap(Udp, 512);"
// 2nd default parameter is COAP_BUF_MAX_SIZE(defaulit:128)
// For UDP fragmentation, it is good to set the maximum under
// 1280byte when using the internet connection.
WiFiUDP udp;
Coap coap(udp, MAX_JSON_SIZE);
StaticJsonDocument<MAX_JSON_SIZE> doc;

// LED STATE
// bool LEDSTATE;

// MOVE COMMANDS
Command commands[MAX_COMMANDS];
int commandsNumber = 0;
int currentCommand = 0;

// Encoder readings
Readings encoderReadings = { NO_READINGS, { 0, 0 } };

// moving
int moveStartTime = 0;
int moveDuration = 0;
int moveSpeed = 0;
int moveDistance = 0;

// sweeping distances
int sweepMeasureTime = 0;
int sweepDelta = 0;
int sweepStartAngle = 0;
int sweepEndAngle = 0;
int sweepCurrentAngle = 0;
int sweepPause = 0;

char eventString[MAX_JSON_SIZE];

void processCommand(JsonObject cmdObj, int i) {
  String cmd = cmdObj["command"];
  Serial.printf("Command[%d] -> %s\n", i, cmd);

  // if (cmd.equals("LED_OFF")) {
  //   digitalWrite(LED, LOW);
  // } else if (cmd.equals("LED_ON")) {
  //   digitalWrite(LED, HIGH);
  // } else
  if (cmd.equals("MOVE_FORWARD")) {
    commands[i].id = MOVE_FORWARD;
    commands[i].params[0] = cmdObj["distance"];
    if (commands[i].params[0] == 0) {
      commands[i].params[1] = 200;
    }
    commands[i].params[1] = cmdObj["speed"];
    if (commands[i].params[1] == 0) {
      commands[i].params[1] = 120;
    }
  } else if (cmd.equals("MOVE_BACKWARD")) {
    commands[i].id = MOVE_BACKWARD;
    commands[i].params[0] = cmdObj["distance"];
    if (commands[i].params[0] == 0) {
      commands[i].params[1] = 200;
    }
    commands[i].params[1] = cmdObj["speed"];
    if (commands[i].params[1] == 0) {
      commands[i].params[1] = 120;
    }
  } else if (cmd.equals("TURN_LEFT")) {
    commands[i].id = TURN_LEFT;
    commands[i].params[0] = cmdObj["distance"];
    if (commands[i].params[0] == 0) {
      commands[i].params[1] = 95;
    }
    commands[i].params[1] = cmdObj["speed"];
    if (commands[i].params[1] == 0) {
      commands[i].params[1] = 80;
    }
  } else if (cmd.equals("TURN_RIGHT")) {
    commands[i].id = TURN_RIGHT;
    commands[i].params[0] = cmdObj["distance"];
    if (commands[i].params[0] == 0) {
      commands[i].params[1] = 95;
    }
    commands[i].params[1] = cmdObj["speed"];
    if (commands[i].params[1] == 0) {
      commands[i].params[1] = 80;
    }
  } else if (cmd.equals("SWEEP_DISTANCE")) {
    commands[i].id = SWEEP_DISTANCE;
    commands[i].params[0] = cmdObj["resolution"];
    if (commands[i].params[0] == 0) {
      commands[i].params[0] = 9;
    }
    commands[i].params[1] = cmdObj["startang"];
    commands[i].params[2] = cmdObj["endang"];
    if (commands[i].params[2] == 0) {
      commands[i].params[2] = 180;
    }
  }
}

// CoAP server endpoint URL
void callback_commands(CoapPacket &packet, IPAddress ip, int port) {
  Serial.println("[Light] ON/OFF");

  // send response
  char p[packet.payloadlen + 1];
  memcpy(p, packet.payload, packet.payloadlen);
  p[packet.payloadlen] = NULL;

  char t[packet.tokenlen + 1];
  memcpy(t, packet.token, packet.tokenlen);
  t[packet.tokenlen] = NULL;

  String message(p);
  String token(t);

  Serial.println(message);
  Serial.println(token);
  Serial.println(ip);
  Serial.println(port);

  // Parse the JSON input
  DeserializationError err = deserializeJson(doc, message);
  // Parse succeeded?
  if (err) {
    Serial.print(F("deserializeJson() returned "));
    Serial.println(err.f_str());
    const char *responseStr = "Can not parse JSON content";
    coap.sendResponse(ip, port, packet.messageid, responseStr, strlen(responseStr), COAP_BAD_REQUEST, COAP_APPLICATION_JSON, reinterpret_cast<const uint8_t *>(&token[0]), packet.tokenlen);
    return;
  }

  if (doc.is<JsonArray>()) {
    // Walk the JsonArray efficiently
    int i = 0;
    for (JsonObject cmd : doc.as<JsonArray>()) {
      processCommand(cmd, i);
      i++;
      if (i >= MAX_COMMANDS) break;
    }
    commandsNumber = i;
    currentCommand = 0;
  } else {
    commandsNumber = 1;
    currentCommand = 0;
    processCommand(doc.as<JsonObject>(), 0);
  }

  const char *responseStr = reinterpret_cast<const char *>(&message[0]);
  coap.sendResponse(ip, port, packet.messageid, responseStr, strlen(responseStr), COAP_CONTENT, COAP_APPLICATION_JSON, reinterpret_cast<const uint8_t *>(&token[0]), packet.tokenlen);
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

boolean nextCommand() {
  currentCommand++;
  if (currentCommand >= commandsNumber) {
    currentCommand = 0;
    commandsNumber = 0;
    return false;
  }
  Serial.printf("Executing command: %d of %d\n", currentCommand + 1, commandsNumber);
  return true;
}

void setup() {
  Serial.begin(115200);
  setupUSSensorsServos();  // setup ultrasound sensors
  setupEncoders();

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  printWifiStatus();

  // LED State
  // pinMode(LED, OUTPUT);
  // digitalWrite(LED, HIGH);
  // LEDSTATE = true;

  // add server url endpoints.
  // can add multiple endpoint urls.
  // exp) coap.server(callback_switch, "switch");
  //      coap.server(callback_env, "env/temp");
  //      coap.server(callback_env, "env/humidity");
  Serial.println("Setup Callback Light");
  coap.server(callback_commands, "commands");

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

  if (commands[currentCommand].id == MOVE_FORWARD || digitalRead(BUTTON) == HIGH) {
    commands[currentCommand].id = NO_COMMAND;
    moveDuration = 0;
    moveDistance = commands[currentCommand].params[0];
    moveSpeed = commands[currentCommand].params[1];
    Serial.printf("### Moving forward - speed: %d, distance(mm): %d, time: %d\n", moveSpeed, moveDistance, moveDuration);
    moveStartTime = millis();
    encoderReadings = { ENCODER, { 0, 0 } };
    moveForward(moveSpeed);
  } else if (commands[currentCommand].id == MOVE_BACKWARD) {
    commands[currentCommand].id = NO_COMMAND;
    moveDuration = 0;
    moveDistance = commands[currentCommand].params[0];
    moveSpeed = commands[currentCommand].params[1];
    Serial.printf("### Moving backward - speed: %d, distance(mm): %d, time: %d\n", moveSpeed, moveDistance, moveDuration);
    moveStartTime = millis();
    encoderReadings = { ENCODER, { 0, 0 } };
    moveBackward(moveSpeed);
  } else if (commands[currentCommand].id == TURN_LEFT) {
    commands[currentCommand].id = NO_COMMAND;
    moveDuration = 0;
    moveDistance = commands[currentCommand].params[0];
    moveSpeed = commands[currentCommand].params[1];
    Serial.printf("### Turning left - speed: %d, distance(mm): %d, time: %d\n", moveSpeed, moveDistance, moveDuration);
    moveStartTime = millis();
    encoderReadings = { ENCODER, { 0, 0 } };
    turnLeftInPlace(moveSpeed);
  } else if (commands[currentCommand].id == TURN_RIGHT) {
    commands[currentCommand].id = NO_COMMAND;
    moveDuration = 0;
    moveDistance = commands[currentCommand].params[0];
    moveSpeed = commands[currentCommand].params[1];
    Serial.printf("### Turning right - speed: %d, distance(mm): %d, time: %d\n", moveSpeed, moveDistance, moveDuration);
    moveStartTime = millis();
    encoderReadings = { ENCODER, { 0, 0 } };
    turnRightInPlace(moveSpeed);
  } else if (commands[currentCommand].id == SWEEP_DISTANCE) {
    commands[currentCommand].id = NO_COMMAND;
    sweepStartAngle = commands[currentCommand].params[1];
    sweepEndAngle = commands[currentCommand].params[2];
    sweepDelta = (sweepEndAngle - sweepStartAngle) / commands[currentCommand].params[0];
    sweepCurrentAngle = sweepStartAngle;
    sweepPause = max(abs(sweepStartAngle - 90) * 5, 50);  // enough time to position servo to start angle
    sweepMeasureTime = millis();
    attachServo(HORIZONTAL_SERVO_INDEX, sweepStartAngle);
    Serial.printf("### Sweepind distances - resolution: %d, start angle(deg): %d, end angle(deg): %d, increment: %d, pause(ms): %d\n", commands[currentCommand].params[0], sweepStartAngle, sweepEndAngle, sweepDelta, sweepPause);
  }

  int currentTime = millis();
  if ((moveDistance > 0)) {
    if (encoderReadings.type == ENCODER && (encoderReadings.readings[0] + encoderReadings.readings[1]) * DISTANCE_PER_PULSE / 2 < moveDistance) {
      encoderReadings = updateSpeed();
      Serial.println(eventString);
      coap.put(remote_ip, 5683, "sensors", eventString, strlen(eventString));
    } else {
      trunMotorsOff();
      moveStartTime = 0;
      moveDistance = 0;
      moveDuration = 0;
      nextCommand();
    }
  }

  if ((sweepDelta > 0 || sweepMeasureTime > 0) && millis() - sweepMeasureTime > sweepPause) {
    if (sweepCurrentAngle <= sweepEndAngle) {
      sweepPause = max(sweepDelta * 10, 50);
      float sweepDistance = readUSDistance(US_SERVO_INDEX);
      sweepMeasureTime = millis();
      if (sweepDistance > 0) {  // ignore zero distance measurements
        // int dist = min(max((int)(sweepDistance * 1000), 0), 100000);
        // int angle = map(dist, 0, 100000, 0, 180);
        moveServo(HORIZONTAL_SERVO_INDEX, min(sweepCurrentAngle + sweepDelta, 180));
        sprintf(eventString, "{\"type\":\"distance\", \"time\":%d, \"angle\":%d, \"distance\":%f}", sweepMeasureTime, sweepCurrentAngle, sweepDistance);
        Serial.println(eventString);
        coap.put(remote_ip, 5683, "sensors", eventString, strlen(eventString));
        sweepCurrentAngle += sweepDelta;
      }
    } else {
      if (sweepDelta != 0) {  // move servo to 90 deg and finish
        sweepDelta = 0;
        moveServo(HORIZONTAL_SERVO_INDEX, 90);
        sprintf(eventString, "{\"type\":\"sweep_end\"}");
        coap.put(remote_ip, 5683, "sensors", eventString, strlen(eventString));
        sweepMeasureTime = millis();
        sweepPause = max(abs(sweepEndAngle - 90) * 20, 50);  // enough time to position servo to start angle
        nextCommand();
      } else {                 // finish sweeping
        sweepMeasureTime = 0;  // deactivating sweeping
        detachServo(HORIZONTAL_SERVO_INDEX);
      }
    }
  }
  // Readings readings = readUSDistance();
  // sprintf(eventString, "{\"type\":\"distance\", \"time\":%d, \"distanceL\":%f, \"distanceR\":%f}", millis(), readings.reading[0], readings.reading[1]);
  // Serial.println(eventString);
  // coap.put(remote_ip, 5683, "sensors/distance", eventString, strlen(eventString));
}
/*
if you change LED, req/res test with coap-client(libcoap), run following.
coap-client -m get coap://(arduino ip addr)/light
coap-client -e "1" -m put coap://(arduino ip addr)/light
coap-client -e "0" -m put coap://(arduino ip addr)/light
*/
