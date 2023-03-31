int servoPin = 19;  // servo svyrzano kym cifrov pin 2
int moyatYgyl;     // ygylyt na servoto – grubo 0 do 180
int pulseWidth;    // promenliva za servoPulse finkciyata
void setup() {
  pinMode(servoPin, OUTPUT);  // opredelya pin 2 kato izhod
}
void servoPulse(int servoPin, int moyatYgyl) {
  pulseWidth = (moyatYgyl * 10) + 600;  // opredelya zabavyaneto
  digitalWrite(servoPin, HIGH);         // vklyuchva servoto
  delayMicroseconds(pulseWidth);        // izchakva mikrosekundi
  digitalWrite(servoPin, LOW);          // izklyuchva servoto
}
void loop() {
  // servoto trygva ot 10 gradusa i se zavyrta do 170
  for (moyatYgyl = 10; moyatYgyl <= 170; moyatYgyl++) {
    servoPulse(servoPin, moyatYgyl);  // izprashta pin i ygyl
    delay(20);                        // opresnyava cikyla
  }
  // servoto trygva ot 170 gradusa i se zavyrta do 10
  for (moyatYgyl = 170; moyatYgyl >= 10; moyatYgyl--) {
    servoPulse(servoPin, moyatYgyl);  // izprashta pin i ygyl
    delay(20);                        // opresnyava cikyla
  }
}