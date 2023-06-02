#include <ESP32Servo.h>

//define sound speed in cm/uS
#define SOUND_SPEED 0.034

Servo myServo;
int const servoPin = 15;
int const trigPin = 5;
int const echoPin = 39;

int potVal;
int angle;
long duration;
float distanceCm;

void setup() {
  myServo.attach(servoPin);
  Serial.begin(115200);      // Starts the serial communication
  pinMode(trigPin, OUTPUT);  // Sets the trigPin as an Output
  pinMode(echoPin, INPUT);   // Sets the echoPin as an Input
}

void loop() {
  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(20);
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);

  // Calculate the distance
  distanceCm = duration * SOUND_SPEED / 2;

  // Prints the distance in the Serial Monitor
  Serial.print("Distance (cm): ");
  Serial.println(distanceCm);
  int dist = min(max((int) (distanceCm * 1000), 0), 100000);
  angle = map(dist, 0, 100000, 0, 180);
  Serial.print(", angle: ");
  Serial.println(angle);
  myServo.write(angle);

  delay(50);
}