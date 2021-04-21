// ---------------------------------------------------------------- //
// Arduino Ultrasoninc Sensor HC-SR04
// Re-writed by Arbi Abdul Jabbaar
// Using Arduino IDE 1.8.7
// Using HC-SR04 Module
// Tested on 17 September 2019
// ---------------------------------------------------------------- //

#define echoPinL 22 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPinL 23 //attach pin D3 Arduino to pin Trig of HC-SR04
#define echoPinR 19 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPinR 21 //attach pin D3 Arduino to pin Trig of HC-SR04

// defines variables
long durationL; // variable for the duration of sound wave travel - left
int distanceL; // variable for the distance measurement - left
long durationR; // variable for the duration of sound wave travel - right
int distanceR; // variable for the distance measurement - right

void setup() {
  pinMode(trigPinL, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPinL, INPUT); // Sets the echoPin as an INPUT - through 10k/22k resistive divider
  pinMode(trigPinR, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPinR, INPUT); // Sets the echoPin as an INPUT - through 10k/22k resistive divider
  Serial.begin(115200); // // Serial Communication is starting with 9600 of baudrate speed
  Serial.println("Ultrasonic Sensors HC-SR04 Test"); // print some text in Serial Monitor
  Serial.println("with Arduino UNO R3");
}
void loop() {
  // Clears the trigPin condition
  digitalWrite(trigPinL, LOW);
  digitalWrite(trigPinR, LOW);
  delayMicroseconds(2);
  
  // Sets the trigPinL HIGH (ACTIVE) for 10 microseconds
  digitalWrite(trigPinL, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinL, LOW);
  // Reads the echoPinL, returns the sound wave travel time in microseconds
  durationL = pulseIn(echoPinL, HIGH);
  
  // Sets the trigPinR HIGH (ACTIVE) for 10 microseconds
  digitalWrite(trigPinR, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinR, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  durationR = pulseIn(echoPinR, HIGH);
  
  // Calculating the distance
  distanceL = durationL * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  distanceR = durationR * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  // Displays the distance on the Serial Monitor
  Serial.printf("DistanceL: %d cm, DistanceR: %d cm\n", distanceL, distanceR);
}
