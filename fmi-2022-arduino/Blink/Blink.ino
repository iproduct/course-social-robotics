#include <math.h> 
#define LED 23
#define POT 15

// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED, OUTPUT);
  Serial.begin (115200);
}

int readPot(){
  int val = analogRead(POT);
  Serial.println(val);
  return val;
}

// the loop function runs over and over again forever
void loop() {
  digitalWrite (LED, HIGH); // vklyuchva svetodioda
  delay (readPot()); // izchakava
  digitalWrite (LED, LOW); // izklyuchva svetodioda
  delay (readPot()); // izchakava                       // wait for a second
}
