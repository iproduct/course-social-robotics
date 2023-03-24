int ledPin = 23;
int buttonPin = 22;
int step = 10;
int coeff = 2;
int direction = 1; // 1 - divide, 0 - multiply
int period = 1000;
int current = 0;
int ledState = HIGH;
int buttonWaitSteps = 20;
int buttonWaitCurrent = 0;

void setup() {
  Serial.begin(115200); // open a serial port
  pinMode(ledPin, OUTPUT);
  pinMode(buttonPin, INPUT);
}

void loop() {
  int button = digitalRead(buttonPin);
  if (button == HIGH && buttonWaitCurrent > buttonWaitSteps) {
    if(direction == 1) {
      period = period / coeff;
    } else {
      period = period * coeff;
    }
    if(period < 30 || period > 2000) {
      direction = 1 - direction;
    }
    current = 0;
    ledState = HIGH;
    buttonWaitCurrent = 0;
  }
  buttonWaitCurrent += 1;  
  Serial.printf("Button: %d, LED state: %d, current: %d, period: %d\n", button, ledState, current, period);
  if(current >= period) {
    ledState = 1 - ledState; // invert LED state
    current = 0;
  }
  digitalWrite(ledPin,ledState);
  current += step;
  delay(step);
}