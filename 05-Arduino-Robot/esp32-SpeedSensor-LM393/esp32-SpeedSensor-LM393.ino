// ---------------------------------------------------------------- //
// Arduino Speed Sensor using LM393 comparator
// ---------------------------------------------------------------- //

#define SpeedL 13   // digital pin for reading the Speed sensor Left
#define SpeedR 18   // digital pin for reading the Speed sensor Right
#define DEBOUNCETIME 1 

volatile int interruptCountL = 0;
volatile int interruptCountR = 0;
uint32_t debounceTimeoutL = 0; 
uint32_t debounceTimeoutR = 0; 
uint32_t debounceTimeoutOldL = 0; 
uint32_t debounceTimeoutOldR = 0; 
long speedCountL = 0;
long speedCountR = 0;
long speedCountOldL = 0;
long speedCountOldR = 0;
// Mutexes for synchronizing counter access between ISRs and main loop
portMUX_TYPE muxL = portMUX_INITIALIZER_UNLOCKED;
portMUX_TYPE muxR = portMUX_INITIALIZER_UNLOCKED;

unsigned long startTime = 0;
unsigned long endTime = 0;
unsigned long currentTime = 0;
float rotL = 0;
float rotR = 0;
float rpsL = 0;
float rpsR = 0;

// Interrupt Service Routines
void IRAM_ATTR isrSpeedL() {
  debounceTimeoutL = xTaskGetTickCount();    // get millis() inside ISR
  if(debounceTimeoutL - debounceTimeoutOldL > DEBOUNCETIME) {
    portENTER_CRITICAL_ISR(&muxL);
    interruptCountL++;
    portEXIT_CRITICAL_ISR(&muxL);
    debounceTimeoutOldL = debounceTimeoutL;
  }
}

void IRAM_ATTR isrSpeedR() {
 debounceTimeoutR = xTaskGetTickCount();    // get millis() inside ISR
  if(debounceTimeoutR - debounceTimeoutOldR > DEBOUNCETIME) {
    portENTER_CRITICAL_ISR(&muxR);
    interruptCountR++;
    portEXIT_CRITICAL_ISR(&muxR);
    debounceTimeoutOldR = debounceTimeoutR;
  }
}

void setup() {
  pinMode(SpeedL, INPUT); // Sets the echoPin as an INPUT
  pinMode(SpeedR, INPUT); // Sets the echoPin as an INP
  attachInterrupt(digitalPinToInterrupt(SpeedL), isrSpeedL, CHANGE ); // attach iterrupt to speed sensor SpeedL pin, detects every change high->low and low->high
  attachInterrupt(digitalPinToInterrupt(SpeedR), isrSpeedR, CHANGE ); // attach iterrupt to speed sensor SpeedR pin, detects every change high->low and low->high
  Serial.begin(115200); // // Serial Communication is starting with 115200 of baudrate speed
  Serial.println("Speed Sensor using LM393 Comparator"); // print some text in Serial Monitor
  startTime = millis();
  endTime = startTime + 1000;
}


void loop() {
//  Serial.printf("InterruptL: %d, InterruptR: %d\n", interruptCountL , interruptCountR);
  
  if (interruptCountL > 0) {
    portENTER_CRITICAL(&muxL);
    speedCountL += interruptCountL;
    interruptCountL = 0;
    portEXIT_CRITICAL(&muxL);
    Serial.printf("An interruptL has occurred. Total: %d\n", speedCountL);
  }

  if (interruptCountR > 0) {
    portENTER_CRITICAL(&muxR);
    speedCountR += interruptCountR;
    interruptCountR = 0;
    portEXIT_CRITICAL(&muxR);
    Serial.printf("An interruptR has occurred. Total: %d\n", speedCountR);
  }

  // Calculating the speed
  currentTime = millis();
  if (currentTime > endTime) {
    rotL = (speedCountL - speedCountOldL) / 40.0;   // 1 rotation = 2 x 20 impulses;
    speedCountOldL = speedCountL;
    rotR = (speedCountR - speedCountOldR) / 40.0;   // 1 rotation = 2 x 20 impulses;
    speedCountOldR = speedCountR;
    rpsL = (rotL * 1000) / (currentTime - startTime); // rotations per second 
    rpsR = (rotR * 1000) / (currentTime - startTime); // rotations per second 
    // Displays the distance on the Serial Monitor
    Serial.printf("RotationsL: %8.2f, RotationsR: %8.2f | SpeedL: %8.2f rps, SpeedR: %8.2f rps\n", rotL, rotR, rpsL, rpsR);
    startTime = millis();
    endTime = startTime + 1000;
  }
  delay(50);
}
