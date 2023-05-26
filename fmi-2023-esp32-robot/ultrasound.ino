//define sound speed in cm/uS
#define SOUND_SPEED 0.034

// Servo myServo;
// int const potPin = 34;
// int const servoPin = 19;
int const trigPinL = 23;
int const echoPinL = 22;
int const trigPinR = 21;
int const echoPinR = 19;

// int potVal;
// int angle;
long duration;
float distanceCmL;
float distanceCmR;

void setupUSSensors() {
  // myServo.attach(servoPin);
  pinMode(trigPinL, OUTPUT);  // Sets the trigPin as an Output
  pinMode(echoPinL, INPUT);   // Sets the echoPin as an Input
  pinMode(trigPinR, OUTPUT);  // Sets the trigPin as an Output
  pinMode(echoPinR, INPUT);   // Sets the echoPin as an Input
}

void triger(int trigPin) {
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
}

Readings readUSDistance() {
  // Triger ultrasound pulse left
  triger(trigPinL);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPinL, HIGH);

  // Calculate the distance
  distanceCmL = duration * SOUND_SPEED / 2;

  // Triger ultrasound pulse right
  triger(trigPinR);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPinR, HIGH);

  // Calculate the distance
  distanceCmR = duration * SOUND_SPEED / 2;

  // Prints the distance in the Serial Monitor
  Serial.print("Distance left (cm): ");
  Serial.print(distanceCmL);
  Serial.print(", Distance right (cm): ");
  Serial.println(distanceCmR);

  Readings result;
  result.type = ULTRASOUND_DISTANCE;
  result.readings[0] = distanceCmL;
  result.readings[1] = distanceCmR;
  // static float MyArray[] = {distanceCmL, distanceCmR};
  return result;

  // int dist = min(max((int) (distanceCm * 1000), 0), 40000);
  // angle = map(dist, 0, 40000, 0, 180);
  // Serial.print(", angle: ");
  // Serial.println(angle);
  // myServo.write(angle);
}