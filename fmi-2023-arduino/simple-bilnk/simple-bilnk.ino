#define MAX_MOISTURE 980      // maximum moisture reading value
#define MIN_MOISTURE 1940      // maximum moisture reading value


const int ledPin1 = 5;
const int ledPin2 = 18;
const int ledPin3 = 19;
const int moistSensorPin01=36;

void setup() {
  Serial.begin(115200);
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
}

void loop() {
  int sensorVal = analogRead(moistSensorPin01);
  int percentageHumididy = map(sensorVal, MAX_MOISTURE, MIN_MOISTURE, 100, 0);
  Serial.printf("%d - %d%%\n", sensorVal, percentageHumididy);
  digitalWrite(ledPin1, HIGH);
  delay(1000);
  digitalWrite(ledPin1, LOW);
  digitalWrite(ledPin2, HIGH);
  delay(1000);
  digitalWrite(ledPin2, LOW);
  digitalWrite(ledPin3, HIGH);
  delay(1000);
  digitalWrite(ledPin3, LOW);
  // delay(20000);
}
