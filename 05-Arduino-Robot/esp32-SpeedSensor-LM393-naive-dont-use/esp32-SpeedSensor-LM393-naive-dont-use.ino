// ---------------------------------------------------------------- //
// Arduino IR Sensor SHARP SNS-GP2Y0A21YK0F + Ultrasoninc Sensor HC-SR04
// ---------------------------------------------------------------- //

#define SpeedL 13   // analog pin for reading the IR sensor
#define SpeedR 18   // analog pin for reading the IR sensor

unsigned long start_time = 0;
unsigned long end_time = 0;
int steps = 0;
int steps_old = 0;
float temp = 0;
float rps = 0;

void setup() {
  pinMode(SpeedL, INPUT); // Sets the echoPin as an INPUT
  pinMode(SpeedR, INPUT); // Sets the echoPin as an INPUT
  Serial.begin(115200); // // Serial Communication is starting with 115200 of baudrate speed
  Serial.println("Speed Sensor using LM393 Comparator"); // print some text in Serial Monitor
}
void loop() {
  start_time = millis();
  end_time = start_time + 1000;
  
  // reads the Speed sensors
  while (millis() < end_time)
  {
    if (digitalRead(SpeedL))
    {
      Serial.printf("StepsL: %d\n",steps);
      steps = steps + 1;
      while (digitalRead(SpeedL));
    }
  }
  temp = steps - steps_old;
  steps_old = steps;

  // Calculating the speed
  temp = steps - steps_old;
  steps_old = steps;
  rps = (temp / 20.0); // rotations per second
  
  // Displays the distance on the Serial Monitor
  Serial.printf("SpeedL: %8.2f rps, SpeedR: %8.2f rps\n",rps, temp);
}
