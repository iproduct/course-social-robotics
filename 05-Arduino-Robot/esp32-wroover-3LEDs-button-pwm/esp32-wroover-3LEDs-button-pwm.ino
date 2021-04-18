#include <Arduino.h>
#include <analogWrite.h>

int ledpinR = 32; //define the LED connect to Pin11;
int ledpinG = 33; //define the LED connect to Pin11;
int ledpinY = 25; //define the LED connect to Pin11;
int inpin = 34; //define the button connect to Pin7
const unsigned long LED_DURATION = 4000;
const int LOOP_DELAY = 20;


int val;//val to store the data
int activeLed = -1; // 0 - 2
float brightness = 0;
float ledStep;
unsigned long switchonTime;

void setup()
{
  pinMode(ledpinR, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(ledpinG, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(ledpinY, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(inpin, INPUT); //define the button Pin to INPUT;
  ledStep = (2.0 * 255 * LOOP_DELAY) / LED_DURATION;
  Serial.begin(115200);
  Serial.println("Starting program");
}
void loop()
{
  val = digitalRead(inpin); //digital read the button pin
  if (val == LOW) //if the button pressed;
  {
    trunLedsOff();
    activeLed = -1;
    brightness = 0;
  }
  else
  {
    if (activeLed == -1) {
      switchonTime = millis();
      activeLed = 0;
      brightness = 0;
    }
    if (millis() - switchonTime > LED_DURATION) {
      switchonTime = millis();
      activeLed = (activeLed + 1) % 3;
      brightness = 0;
    }
    brightness += ledStep;
    if (brightness < 0 || brightness > 255) {
      ledStep = -ledStep;
    }
    if(brightness > 255){
      brightness = 255;
    }
    if(brightness < 0){
      brightness = 0;
    }
//    Serial.printf("Active LED: %d, Step: %f, Brightness: %f\n", activeLed, ledStep, brightness);
    setLedBrightness(activeLed, round(brightness));
    delay(LOOP_DELAY);
  }
}

void trunLedsOff() {
  analogWrite(ledpinR, 0); //turn off the LED
  analogWrite(ledpinG, 0); //turn off the LED
  analogWrite(ledpinY, 0); //turn off the LED
}

void setLedBrightness(int activeLed, int brightness) {
  switch (activeLed) {
    case 0: analogWrite(ledpinY, 0); analogWrite(ledpinR, brightness);  break;
    case 1: analogWrite(ledpinR, 0); analogWrite(ledpinG, brightness); break;
    case 2: analogWrite(ledpinG, 0); analogWrite(ledpinY, brightness); break;
  }
}
