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
float speed = 0;
float step;
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
  step = (2.0 * 255 * LOOP_DELAY) / DURATION;
  Serial.begin(115200);
  Serial.println("Starting program");
}
void loop()
{
  val = digitalRead(button); //digital read the button pin
  if (val == LOW) //if the button is not pressed;
  {
    trunMotorsOff();
    started = false;
  }
  else
  {
    if (!started) {
      switchonTime = millis();
      started = true;
      speed = 0;
      setMotorDirection(1, CLOCKWISE);
      setMotorDirection(2, CLOCKWISE);
    }
    speed += step;
    if (speed < 0 || speed > 255) {
      step = -step;
    }
    if (speed > 255) {
      speed = 255;
    }
    if (speed < 0) {
      speed = 0;
    }
    Serial.printf("Active Motor: %d, Step: %f, Speed %f\n", 2, step, speed);
    setMotorSpeed(1, round(speed));
    setMotorSpeed(2, round(speed));
    delay(LOOP_DELAY);
  }
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
      digitalWrite(motor == 1 ? DRV_A_IN1: DRV_B_IN1, HIGH);
      digitalWrite(motor == 1 ? DRV_A_IN2: DRV_B_IN2, LOW);
      break;
    case COUNTER_CLOCKWISE:       
      digitalWrite(motor == 1 ? DRV_A_IN1: DRV_B_IN1, LOW);
      digitalWrite(motor == 1 ? DRV_A_IN2: DRV_B_IN2, HIGH);
      break;

  }
}

void setMotorSpeed(int motor, int speed) {
  switch (motor) {
    case 1: analogWrite(DRV_A_PWM, speed); break;
    case 2: analogWrite(DRV_B_PWM, speed); break;
  }
}
