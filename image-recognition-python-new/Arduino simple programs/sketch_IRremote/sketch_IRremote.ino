#include <boarddefs.h>
#include <IRremote.h>
#include <IRremoteInt.h>

int RECV_PIN = 8; // IR Receiver - Arduino Pin Number 8
IRrecv irrecv(RECV_PIN);
decode_results results;
int redPin = 5;
int greenPin = 6;
int bluePin = 7;
 
void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);  
}
 
void loop() {
  
  if (irrecv.decode(&results)) {   
    
    if (results.value == 0xF21D7D46) { // Red Button
      setColor(255, 0, 0);  // Sets Red Color to the RGB LED
      delay(100);
      }
    if (results.value == 0x87CF1B29) { // Green Button
       setColor(0, 255, 0);  // Green Color
      delay(100);
      }
    if (results.value == 0x6623D37C) { // Yellow Button
      setColor(255, 255, 0);  // Yellow Color
      delay(100);
      }   
    if (results.value == 0x854115F2) { // Blue Button
      setColor(0, 0, 255);  // Blue Color
      delay(100);
      }
     if (results.value == 0x1639AB6E) { // Stop Button
      setColor(0, 0, 0);  // OFF
      delay(100);
      }
      
    irrecv.resume(); // Receive the next value
  }
  delay(100);
}
// Custom made function for activating the RGB LED 
void setColor(int red, int green, int blue)
{
  analogWrite(redPin, red); // Sends PWM signal to the Red pin
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);  
}
