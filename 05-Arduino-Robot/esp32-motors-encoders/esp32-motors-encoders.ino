#include <Arduino.h>
#include <analogWrite.h>

// define motor driver pins and constants
#define DRV_A_IN1 26
#define DRV_A_IN2 27
#define DRV_A_PWM 14

#define DRV_B_IN1 32
#define DRV_B_IN2 33
#define DRV_B_PWM 25

#define CLOCKWISE 1
#define COUNTER_CLOCKWISE 2

// define encoder pins
#define ENCODER_L 13   // digital pin for reading the Speed sensor Left
#define ENCODER_R 18   // digital pin for reading the Speed sensor Right
#define SPEED_CALC_PERIOD 50  // in miliseconds
#define DEBOUNCETIME 1         // in miliseconds


// define button pin
#define BUTTON 12

volatile int interruptCountL = 0;
volatile int interruptCountR = 0;
uint32_t debounceTimeoutL = 0;
uint32_t debounceTimeoutR = 0;
uint32_t debounceTimeoutOldL = 0;
uint32_t debounceTimeoutOldR = 0;
long encoderCountL = 0;
long encoderCountR = 0;
long encoderCountOldL = 0;
long encoderCountOldR = 0;
// Mutexes for synchronizing counter access between ISRs and main loop
portMUX_TYPE muxL = portMUX_INITIALIZER_UNLOCKED;
portMUX_TYPE muxR = portMUX_INITIALIZER_UNLOCKED;

unsigned long startTime = 0;
unsigned long endTime = 0;
unsigned long currentTime = 0;
float rotL = 0;
float rotR = 0;
float rpsL = 0;
float rpsR = 0;

// Interrupt Service Routines
void IRAM_ATTR isrSpeedL() {
  debounceTimeoutL = xTaskGetTickCount();    // get millis() inside ISR
  if (debounceTimeoutL - debounceTimeoutOldL > DEBOUNCETIME) {
    portENTER_CRITICAL_ISR(&muxL);
    interruptCountL++;
    portEXIT_CRITICAL_ISR(&muxL);
    debounceTimeoutOldL = debounceTimeoutL;
  }
}

void IRAM_ATTR isrSpeedR() {
  debounceTimeoutR = xTaskGetTickCount();    // get millis() inside ISR
  if (debounceTimeoutR - debounceTimeoutOldR > DEBOUNCETIME) {
    portENTER_CRITICAL_ISR(&muxR);
    interruptCountR++;
    portEXIT_CRITICAL_ISR(&muxR);
    debounceTimeoutOldR = debounceTimeoutR;
  }
}

void setup()
{
  pinMode(DRV_B_IN1, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_IN2, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN1, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN2, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(BUTTON, INPUT); //define the button Pin to INPUT;

  // encoders reading setup
  pinMode(ENCODER_L, INPUT); // Sets the echoPin as an INPUT
  pinMode(ENCODER_R, INPUT); // Sets the echoPin as an INP
  attachInterrupt(digitalPinToInterrupt(ENCODER_L), isrSpeedL, CHANGE ); // attach iterrupt to speed sensor SpeedL pin, detects every change high->low and low->high
  attachInterrupt(digitalPinToInterrupt(ENCODER_R), isrSpeedR, CHANGE ); // attach iterrupt to speed sensor SpeedR pin, detects every change high->low and low->high
  Serial.begin(115200); // // Serial Communication is starting with 115200 of baudrate speed
  Serial.println("Motors + encoders demo"); // print some text in Serial Monitor
  startTime = millis();
  endTime = startTime + SPEED_CALC_PERIOD;

}
void loop()
{
  // motors demo
  int val = digitalRead(BUTTON); //digital read the button pin
  if (val == HIGH) //if the button is not pressed;
  {
    int speed = 120;
    int timeForwardMs = 2000;
    int timeTurnRightMs = 450;
    for ( int i = 0; i < 4; i++) {
      Serial.printf("Moving forward - speed: %d, time: %d\n", speed, timeForwardMs);
      moveForward(speed, timeForwardMs);
      Serial.printf("Turning right in place - speed: %d, time: %d\n", speed, timeTurnRightMs);
      turnRightInPlace(speed, timeTurnRightMs);
      //    moveBackward(speed, timeMs);
    }
  }
  delay(50);
}

void trunMotorsOff() {
  digitalWrite(DRV_A_IN1, LOW);
  digitalWrite(DRV_A_IN2, LOW);
  digitalWrite(DRV_B_IN1, LOW);
  digitalWrite(DRV_B_IN2, LOW);
}

void setMotorDirection(int motor, int direction) {
  switch (direction) {
    case CLOCKWISE:
      digitalWrite(motor == 1 ? DRV_A_IN1 : DRV_B_IN1, HIGH);
      digitalWrite(motor == 1 ? DRV_A_IN2 : DRV_B_IN2, LOW);
      break;
    case COUNTER_CLOCKWISE:
      digitalWrite(motor == 1 ? DRV_A_IN1 : DRV_B_IN1, LOW);
      digitalWrite(motor == 1 ? DRV_A_IN2 : DRV_B_IN2, HIGH);
      break;

  }
}

void setMotorSpeed(int motor, int speed) {
  switch (motor) {
    case 1: analogWrite(DRV_A_PWM, speed); break;
    case 2: analogWrite(DRV_B_PWM, speed); break;
  }
}

// encoders
void updateEncoderData() {
  if (interruptCountL > 0) {
    portENTER_CRITICAL(&muxL);
    encoderCountL += interruptCountL;
    interruptCountL = 0;
    portEXIT_CRITICAL(&muxL);
    Serial.printf("An interruptL has occurred. Total: %d\n", encoderCountL);
  }

  if (interruptCountR > 0) {
    portENTER_CRITICAL(&muxR);
    encoderCountR += interruptCountR;
    interruptCountR = 0;
    portEXIT_CRITICAL(&muxR);
    Serial.printf("An interruptR has occurred. Total: %d\n", encoderCountR);
  }

  // Calculating the speed
  currentTime = millis();
  if (currentTime > endTime) {
    rotL = (encoderCountL - encoderCountOldL) / 40.0;   // 1 rotation = 2 x 20 impulses;
    encoderCountOldL = encoderCountL;
    rotR = (encoderCountR - encoderCountOldR) / 40.0;   // 1 rotation = 2 x 20 impulses;
    encoderCountOldR = encoderCountR;
    rpsL = (rotL * 1000) / (currentTime - startTime); // rotations per second
    rpsR = (rotR * 1000) / (currentTime - startTime); // rotations per second
    // Displays the distance on the Serial Monitor
    Serial.printf("RotationsL: %8.2f, RotationsR: %8.2f | SpeedL: %8.2f rps, SpeedR: %8.2f rps\n", rotL, rotR, rpsL, rpsR);
    startTime = millis();
    endTime = startTime + SPEED_CALC_PERIOD;
  }
}

void resetEncoderCounters() {
  encoderCountL = 0;
  encoderCountR = 0;
  encoderCountOldL = 0;
  encoderCountOldR = 0;
}

// higher order commands
void moveForward(int speed, int timeMs) {
  int startTime = millis();
  int endTime = startTime + timeMs;
  int delayMs = timeMs / SPEED_CALC_PERIOD;
  int speedL = speed;
  int speedR = speed;
  setMotorDirection(1, COUNTER_CLOCKWISE);
  setMotorDirection(2, COUNTER_CLOCKWISE);
  resetEncoderCounters(); // zero encoder counters
  while (millis() < endTime) {
    updateEncoderData();
    int difference = encoderCountR - encoderCountL;
    if (difference > 0) {
      speedR += 1;
    }
    if (difference < 0) {
      speedL += 1;
    }
    if (speedL > speed) {
      speedR -= speedL - speed;
      speedL = speed;
    }
    if (speedR > speed) {
      speedL -= speedR - speed;
      speedR = speed;
    }

    setMotorSpeed(1, speedL);
    setMotorSpeed(2, speedR);
    Serial.printf("!!! EncoderL: %d, EncoderR: %d, SpeedL: %d, SpeedR: %d\n", encoderCountL, encoderCountR, speedL, speedR);
    delay(SPEED_CALC_PERIOD);
  }
  trunMotorsOff();
}

void moveBackward(int speed, int timeMs) {
  setMotorDirection(1, CLOCKWISE);
  setMotorDirection(2, CLOCKWISE);
  setMotorSpeed(1, speed);
  setMotorSpeed(2, speed);
  delay(timeMs);
  trunMotorsOff();
}

void turnLeftInPlace(int speed, int timeMs) {
  setMotorDirection(1, COUNTER_CLOCKWISE);
  setMotorDirection(2, CLOCKWISE);
  setMotorSpeed(1, speed);
  setMotorSpeed(2, speed);
  delay(timeMs);
  trunMotorsOff();
}

void turnRightInPlace(int speed, int timeMs) {
  setMotorDirection(1, CLOCKWISE);
  setMotorDirection(2, COUNTER_CLOCKWISE);
  setMotorSpeed(1, speed);
  setMotorSpeed(2, speed);
  delay(timeMs);
  trunMotorsOff();
}
