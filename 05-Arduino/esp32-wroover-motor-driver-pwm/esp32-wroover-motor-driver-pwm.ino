#include <Arduino.h>
#include <analogWrite.h>

int DRV_B_IN1 = 32; //define the LED connect to Pin11;
int DRV_B_IN2 = 33; //define the LED connect to Pin11;
int DRV_A_PWM = 25; //define the LED connect to Pin11;
int DRV_B_PWM = 26; //define the LED connect to Pin11;
int inpin = 34; //define the button connect to Pin7
const unsigned long DURATION = 4000;
const int LOOP_DELAY = 500;


int val;//val to store the data
boolean started;
float speed = 0;
float step;
unsigned long switchonTime;

void setup()
{
  pinMode(DRV_B_IN1, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_IN2, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_B_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(DRV_A_PWM, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(inpin, INPUT); //define the button Pin to INPUT;
  step = (2.0 * 255 * LOOP_DELAY) / DURATION;
  Serial.begin(115200);
  Serial.println("Starting program");
}
void loop()
{
  val = digitalRead(inpin); //digital read the button pin
  if (val == LOW) //if the button pressed;
  {
    trunMotorsOff();
    started = false;
  }
  else
  {
    if (!strated) {
      switchonTime = millis();
      started = true;
      speed = 0;
    }
    speed += step;
    if (speed < 0 || speed > 255) {
      step = -step;
    }
    if(speed > 255){
      speed = 255;
    }
    if(speed < 0){
      speed = 0;
    }
    Serial.printf("Active Motor: %d, Step: %f, Brightness: %f\n", 2, step, speed);
    setMotorSpeed(2, round(speed));
    delay(LOOP_DELAY);
  }
}

void trunMotorsOff() {
  digitalWrite(DRV_B_IN1, LOW); //turn off the LED
  digitalWrite(DRV_B_IN2, LOW); //turn off the LED
}

void setMotorSpeed(int motor, int speed) {
  switch (motor) {
    case 1: analogWrite(DRV_A_PWM, 0); break;
    case 2: analogWrite(DRV_B_PWM, 0); break;
  }
}
