// ---------------------------------------------------------------- //
// Arduino IR Sensor SHARP SNS-GP2Y0A21YK0F + Ultrasoninc Sensor HC-SR04
// ---------------------------------------------------------------- //

#define IRpinL 35   // analog pin for reading the IR sensor - through resistive divider 10k/22k: 5V -> 3.3V
#define IRpinR 34   // analog pin for reading the IR sensor - through resistive divider 10k/22k: 5V -> 3.3V
#define echoPinL 22 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPinL 23 // attach pin D3 Arduino to pin Trig of HC-SR04
#define echoPinR 19 // attach pin D2 Arduino to pin Echo of HC-SR04
#define trigPinR 21 // attach pin D3 Arduino to pin Trig of HC-SR04

// defines variables
long durationL; // variable for the duration of sound wave travel - left
int usDistanceL; // variable for the distance measurement - left
long durationR; // variable for the duration of sound wave travel - right
int usDistanceR; // variable for the distance measurement - right


void setup() {
  pinMode(trigPinL, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPinL, INPUT); // Sets the echoPin as an INPUT
  pinMode(trigPinR, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPinR, INPUT); // Sets the echoPin as an INPUT
  Serial.begin(115200); // // Serial Communication is starting with 9600 of baudrate speed
  Serial.println("IR Sensor SHARP SNS-GP2Y0A21YK0F and Ultrasonic Sensor HC-SR04 Test"); // print some text in Serial Monitor
  Serial.println("with ESP3-WROOWER");
}
void loop() {
  // reads the IR distance sensors
  float voltsL = analogRead(IRpinL) * 0.0048828125 * 0.96; // value from sensor * (3.3/1024) * 3.3/5 * (10 + 22)/22
  float voltsR = analogRead(IRpinR) * 0.0048828125 * 0.96; // value from sensor * (3.3/1024) * 3.3/5 * (10 + 22)/22
  float irDistanceL = 65 * pow(voltsL, -1.10);      // worked out from graph 65
  float irDistanceR = 65 * pow(voltsR, -1.10);      // worked out from graph 65

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
  usDistanceL = durationL * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  usDistanceR = durationR * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  // Displays the distance on the Serial Monitor
  //  Serial.printf("IR-DistanceL: %f cm, IR-DistanceR: %f cm\n", irDistanceL, irDistanceR);
  Serial.printf("US-DistanceL: %d cm, US-DistanceR: %d cm, IR-DistanceL: %8.2f cm, IR-DistanceR: %8.2f cm\n", 
    usDistanceL, usDistanceR, irDistanceL, irDistanceR);
}
