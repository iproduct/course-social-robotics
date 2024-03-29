const int ESP32_LED = 23;

void setup() {
  pinMode(ESP32_LED, OUTPUT);
  Serial.begin(115200);
}

void loop() {
  Serial.write("LED: ON ");
  digitalWrite(ESP32_LED, HIGH);
  delay(1000);
  Serial.write("LED: OFF ");
  digitalWrite(ESP32_LED, LOW);
  delay(1000);
}
