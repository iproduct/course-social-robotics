/*
  Blink

  Turns an LED on for one second, then off for one second, repeatedly.

  Most Arduinos have an on-board LED you can control. On the UNO, MEGA and ZERO
  it is attached to digital pin 13, on MKR1000 on pin 6. LED_BUILTIN is set to
  the correct LED pin independent of which board is used.
  If you want to know what pin the on-board LED is connected to on your Arduino
  model, check the Technical Specs of your board at:
  https://docs.arduino.cc/hardware/

  modified 8 May 2014
  by Scott Fitzgerald
  modified 2 Sep 2016
  by Arturo Guadalupi
  modified 8 Sep 2016
  by Colby Newman

  This example code is in the public domain.

  https://docs.arduino.cc/built-in-examples/basics/Blink/
*/
#include <stdio.h>
#define GEEN_LED 3
#define BUTTON 2

const int MAX_DELAY_COUNT = 128;
int period = 10;
int lastButtonState = LOW;
char buffer [256];
int delayCount = 0;
int currentMaxDelayCount = MAX_DELAY_COUNT;
int ledState = LOW;
int increaseFreq = true;

// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(GEEN_LED, OUTPUT);
  pinMode(BUTTON, INPUT);
  // initialize serial communication:
  Serial.begin(9600);
}

// the loop function runs over and over again forever
void loop() {
  int buttonState = digitalRead(BUTTON);
  if (buttonState != lastButtonState && buttonState == HIGH) { //only check if button state changes
    if(currentMaxDelayCount < 2 || currentMaxDelayCount > MAX_DELAY_COUNT) {
        increaseFreq = !increaseFreq;
    }
    if(increaseFreq) {
      currentMaxDelayCount /= 2;
    } else {
      currentMaxDelayCount *= 2;
    }
    sprintf(buffer, "{\"delay\":%d\n", currentMaxDelayCount);
    Serial.print(buffer);
  }
  lastButtonState = buttonState;

  if (delayCount >= currentMaxDelayCount) {
    ledState = !ledState;
    delayCount = 0;
  }
  digitalWrite(GEEN_LED, ledState);  // change state of the LED by setting the pin to the HIGH voltage level
  delay(period);      
  delayCount++;                // wait for a second           // wait for a second
}
