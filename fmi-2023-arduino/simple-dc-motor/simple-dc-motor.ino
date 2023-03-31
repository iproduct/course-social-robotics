int motorPin = 21;  // izhoden pin za MOSFET-a
void setup() {
  pinMode(motorPin, OUTPUT);  // zadava pin 5 kato izhod
}
void loop() {
  for (int i = 0; i <= 10; i++)
    for (int j = 0; j < 100; j++) {
      digitalWrite(motorPin, HIGH);  // vklyuchva MOSFET-a
      delay(i);                      // izchakva ¼ sekunda
      digitalWrite(motorPin, LOW);   // izklyuchva MOSFET-a
      delay(10 - i);                 // izchakva ¼ sekunda
    }
  // delay(1000);  // izchakva edna sekunda
}