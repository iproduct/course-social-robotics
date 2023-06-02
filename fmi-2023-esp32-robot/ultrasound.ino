//define sound speed in cm/uS
#define SOUND_SPEED 0.034
#define NUM_US_SENSORS 3  // number of ultarsound sensors
#define NUM_SERVOS 2      //number of servos

Servo servo1;
Servo servo2;

Servo servos[] = { servo1, servo2 };
int usSensorTrigerPins[] = { 23, 21, 5 };  // Trigger pins - Left, Right, Servo-driven
int usSensorEchoPins[] = { 22, 19, 39 };   // Echo pins - Left, Right, Servo-driven
int servoPins[] = { 4, 15 };               //Servo pins - horizontal, vertical
int servoAngles[] = { 90, 110 };           // current servo angles

void setupUSSensorsServos() {
  for (int i = 0; i < NUM_US_SENSORS; i++) {
    pinMode(usSensorTrigerPins[i], OUTPUT);  // Sets the trigPin as an Output
    pinMode(usSensorEchoPins[i], INPUT);     // Sets the echoPin as an Input
  }
  // for(int i = 0; i < NUM_SERVOS; i++) {
  //    servos[i].attach(servoPins[i]);  // Sets the trigPin as an Output
  //    moveServo(i, servoAngles[i]);
  // }
}

void triger(int trigPin) {
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(20);
  digitalWrite(trigPin, LOW);
}

float readUSDistance(int sensorIndex) {
  // Triger ultrasound pulse left
  triger(usSensorTrigerPins[sensorIndex]);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  long duration = pulseIn(usSensorEchoPins[sensorIndex], HIGH);

  // Calculate the distance
  float distanceCm = duration * SOUND_SPEED / 2;

  // Prints the distance in the Serial Monitor
  Serial.printf("Distance (cm) - Sensor %d: %f\n", sensorIndex, distanceCm);

  // Readings result;
  // result.type = ULTRASOUND_DISTANCE;
  // result.readings[0] = distanceCmL;
  // result.readings[1] = distanceCmR;
  // static float MyArray[] = {distanceCmL, distanceCmR};
  return distanceCm;
}

void attachServo(int index, int initialAngle) {
  servos[index].attach(servoPins[index]);  // Attaches servo to the servoPin[index]
  moveServo(index, initialAngle);
}
void detachServo(int index) {
  servos[index].detach();  // Sets the trigPin as an Output
}
void moveServo(int index, int angle) {
  int dist = min(max(angle, 0), 180);
  // angle = map(dist, 0, 100000, 0, 180);
  Serial.printf("Servo [%d] angle: %d\n", index, angle);
  Serial.println(angle);
  servos[index].write(angle);
  servoAngles[index] = angle;
}