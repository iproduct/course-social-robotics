#include <Arduino.h>
#include <analogWrite.h>
int DRV_A_IN1 = 26;
int DRV_A_IN2 = 27;
int DRV_A_PWM = 14;

int DRV_B_IN1 = 32;
int DRV_B_IN2 = 33;
int DRV_B_PWM = 25;

int button = 12; //define the button connect to Pin7
const unsigned long DURATION = 8000;
const int LOOP_DELAY = 500;

const int CLOCKWISE = 1;
const int COUNTER_CLOCKWISE = 2;

int val;//val to store the data
bool started;
int speed = 0;
unsigned long switchonTime;

void setup()
{
  pinMode(DRV_B_IN1, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_IN2, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN1, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN2, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(button, INPUT); //define the button Pin to INPUT;
  Serial.begin(115200);
  Serial.println("Starting program");
}
void loop()
{
  val = digitalRead(button); //digital read the button pin
  if (val == HIGH) //if the button is not pressed;
  {
    speed = 120;
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

// higher order commands
void moveForward(int speed, int timeMs) {
  setMotorDirection(1, COUNTER_CLOCKWISE);
  setMotorDirection(2, COUNTER_CLOCKWISE);
  setMotorSpeed(1, speed);
  setMotorSpeed(2, speed);
  delay(timeMs);
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
