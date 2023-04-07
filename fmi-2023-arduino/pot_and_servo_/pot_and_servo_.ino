#include <ESP32Servo.h>

Servo myServo;
int const potPin = 34;
int const servoPin = 19;

int potVal;
int angle;

void setup() {
 myServo.attach(servoPin);
 Serial.begin(115200);
}

void loop() {
 potVal = analogRead(potPin);
 Serial.print("potVal: ");
 Serial.print(potVal);
 angle = map(potVal, 0, 4095, 0, 180);
 Serial.print(", angle: ");
 Serial.println(angle);
 myServo.write(angle);
 delay(15);
}