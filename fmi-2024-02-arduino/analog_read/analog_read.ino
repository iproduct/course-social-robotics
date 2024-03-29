#include <ESP32Servo.h>

#define potPin 36
#define ledPin 23
#define servoPin 22
int potVal;
int freq;
int angle;
Servo myServo;

void setup() {
  pinMode(ledPin, OUTPUT);
  myServo.attach(servoPin);
  Serial.begin(115200);
}

void loop() {
  potVal = analogRead(potPin);
  Serial.print("potVal: ");
  Serial.println(potVal);

  angle = map(potVal, 0, 4095, 0, 179);
  myServo.write(angle);

  // freq = map(potVal, 0, 4095, 1, 40);
  // digitalWrite(ledPin, LOW);
  // delay(500/freq);
  // digitalWrite(ledPin, HIGH);
  // delay(500/freq);
}
