const int ESP32_LED = 23;

void setup() {
  pinMode(ESP32_LED, OUTPUT);
  Serial.begin(9600);
  Serial1.begin(9600);
}

void loop() {
  if (Serial1.available()) {
      Serial.write("Data: ");
      Serial.write(Serial1.read()); 
      Serial.println(); 
  }
  digitalWrite(ESP32_LED, HIGH);
  delay(400);
  digitalWrite(ESP32_LED, LOW);
  delay(200);
}
