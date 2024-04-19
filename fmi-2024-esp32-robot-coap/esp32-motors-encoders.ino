// define motor driver pins and constants
#define DRV_A_IN1 26
#define DRV_A_IN2 27
#define DRV_A_PWM 14

#define DRV_B_IN1 32
#define DRV_B_IN2 33
#define DRV_B_PWM 25

#define CLOCKWISE 1
#define COUNTER_CLOCKWISE 2
#define MOTOR_R 1
#define MOTOR_L 2

// define encoder pins
#define ENCODER_L 13          // digital pin for reading the Speed sensor Left
#define ENCODER_R 18          // digital pin for reading the Speed sensor Right
#define SPEED_CALC_PERIOD 50  // in miliseconds
#define DEBOUNCETIME 18       // in miliseconds

int debounceTime = DEBOUNCETIME;

volatile int interruptCountL = 0;
volatile int interruptCountR = 0;
uint32_t debounceTimeoutL = 0;
uint32_t debounceTimeoutR = 0;
uint32_t debounceTimeoutOldL = 0;
uint32_t debounceTimeoutOldR = 0;
long encoderCountL = 0;
long encoderCountR = 0;
long encoderCountOldL = 0;
long encoderCountOldR = 0;
int speedL = 0;
int speedR = 0;
// Mutexes for synchronizing counter access between ISRs and main loop
portMUX_TYPE muxL = portMUX_INITIALIZER_UNLOCKED;
portMUX_TYPE muxR = portMUX_INITIALIZER_UNLOCKED;

unsigned long encoderPrevReadTime = 0;

float rotL = 0;
float rotR = 0;
float rpsL = 0;
float rpsR = 0;

// Interrupt Service Routines
void IRAM_ATTR isrSpeedL() {
  debounceTimeoutL = xTaskGetTickCount();  // get millis() inside ISR
  if (debounceTimeoutL - debounceTimeoutOldL > debounceTime) {
    interruptCountL++;
    debounceTimeoutOldL = debounceTimeoutL;
  }
}

void IRAM_ATTR isrSpeedR() {
  debounceTimeoutR = xTaskGetTickCount();  // get millis() inside ISR
  if (debounceTimeoutR - debounceTimeoutOldR > debounceTime) {
    interruptCountR++;
    debounceTimeoutOldR = debounceTimeoutR;
  }
}

void setupEncoders() {
  pinMode(DRV_B_IN1, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(DRV_B_IN2, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(DRV_B_PWM, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN1, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(DRV_A_IN2, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(DRV_A_PWM, OUTPUT);  //define the LED Pin to OUTPUT;
  pinMode(BUTTON, INPUT);      //define the button Pin to INPUT;

  // encoders reading setup
  pinMode(ENCODER_L, INPUT);                                             // Sets the echoPin as an INPUT
  pinMode(ENCODER_R, INPUT);                                             // Sets the echoPin as an INP
  attachInterrupt(digitalPinToInterrupt(ENCODER_L), isrSpeedL, CHANGE);  // attach iterrupt to speed sensor SpeedL pin, detects every change high->low and low->high
  attachInterrupt(digitalPinToInterrupt(ENCODER_R), isrSpeedR, CHANGE);  // attach iterrupt to speed sensor SpeedR pin, detects every change high->low and low->high                                                // // Serial Communication is starting with 115200 of baudrate speed
  Serial.println("Motors + encoders demo");                              // print some text in Serial Monitor
  // startTime = millis();
  // endTime = startTime + SPEED_CALC_PERIOD;
}

void loopEncoders() {
  // motors demo
  int val = digitalRead(BUTTON);  //digital read the button pin
  if (val == HIGH)                //if the button is pressed;
  {
    int speed = 120;
    int timeForwardMs = 4000;
    int timeTurnRightMs = 450;
    Serial.printf("Moving forward - speed: %d, time: %d\n", speed, timeForwardMs);
    moveForward(speed);
    // for ( int i = 0; i < 4; i++) {
    //   Serial.printf("Moving forward - speed: %d, time: %d\n", speed, timeForwardMs);
    //   moveForward(speed, timeForwardMs);
    //   Serial.printf("Turning right in place - speed: %d, time: %d\n", speed, timeTurnRightMs);
    //   turnRightInPlace(speed, timeTurnRightMs);
    //   //    moveBackward(speed, timeMs);
    // }
  }
  // delay(50);
}

void trunMotorsOff() {
  digitalWrite(DRV_A_IN1, LOW);
  digitalWrite(DRV_A_IN2, LOW);
  digitalWrite(DRV_B_IN1, LOW);
  digitalWrite(DRV_B_IN2, LOW);
}

void setMotorDirection(int motor, int direction) {
  switch (direction) {
    case CLOCKWISE:
      digitalWrite(motor == 1 ? DRV_A_IN1 : DRV_B_IN1, HIGH);
      digitalWrite(motor == 1 ? DRV_A_IN2 : DRV_B_IN2, LOW);
      break;
    case COUNTER_CLOCKWISE:
      digitalWrite(motor == 1 ? DRV_A_IN1 : DRV_B_IN1, LOW);
      digitalWrite(motor == 1 ? DRV_A_IN2 : DRV_B_IN2, HIGH);
      break;
  }
}

void setMotorSpeed(int motor, int speed) {
  switch (motor) {
    case 1: analogWrite(DRV_A_PWM, speed); break;
    case 2: analogWrite(DRV_B_PWM, speed); break;
  }
}

// encoders
void updateEncoderData() {
  if (interruptCountL > 0) {
    encoderCountL += interruptCountL;
    interruptCountL = 0;
    Serial.printf("An interruptL has occurred. Total: %d\n", encoderCountL);
  }

  if (interruptCountR > 0) {
    encoderCountR += interruptCountR;
    interruptCountR = 0;
    Serial.printf("An interruptR has occurred. Total: %d\n", encoderCountR);
  }

  // Calculating the speed
  int currentTime = millis();
  if (currentTime - moveStartTime < moveDuration && encoderPrevReadTime > 0) {
    rotL = (encoderCountL - encoderCountOldL) / 40.0;  // 1 rotation = 2 x 20 impulses;
    encoderCountOldL = encoderCountL;
    rotR = (encoderCountR - encoderCountOldR) / 40.0;  // 1 rotation = 2 x 20 impulses;
    encoderCountOldR = encoderCountR;
    int deltaT = currentTime - encoderPrevReadTime;
    rpsL = (rotL * 1000) / deltaT;  // rotations per second
    rpsR = (rotR * 1000) / deltaT;  // rotations per second
    // Displays the distance on the Serial Monitor
    Serial.printf("RotationsL: %8.2f, RotationsR: %8.2f | SpeedL: %8.2f rps, SpeedR: %8.2f rps\n", rotL, rotR, rpsL, rpsR);
  }
  encoderPrevReadTime = currentTime;
}

void resetEncoderCounters() {
  encoderCountL = 0;
  encoderCountR = 0;
  encoderCountOldL = 0;
  encoderCountOldR = 0;
  portENTER_CRITICAL_ISR(&muxL);
  interruptCountL = 0;
  portEXIT_CRITICAL_ISR(&muxL);
  portENTER_CRITICAL_ISR(&muxR);
  interruptCountR = 0;
  portEXIT_CRITICAL_ISR(&muxR);
}

// higher order commands
void moveForward(int speed) {
  speedL = speed;
  speedR = speed;
  debounceTime = DEBOUNCETIME;
  resetEncoderCounters();  // zero encoder counters
  setMotorSpeed(MOTOR_L, speedL);
  setMotorSpeed(MOTOR_R, speedR);
  setMotorDirection(MOTOR_L, COUNTER_CLOCKWISE);
  setMotorDirection(MOTOR_R, COUNTER_CLOCKWISE);
}

void moveBackward(int speed) {
  speedL = speed;
  speedR = speed;
  debounceTime = DEBOUNCETIME;
  resetEncoderCounters();  // zero encoder counters
  setMotorSpeed(MOTOR_L, speedL);
  setMotorSpeed(MOTOR_R, speedR);
  setMotorDirection(MOTOR_L, CLOCKWISE);
  setMotorDirection(MOTOR_R, CLOCKWISE);
}

void turnLeftInPlace(int speed) {
  speedL = speed;
  speedR = speed;
  debounceTime = DEBOUNCETIME;
  resetEncoderCounters();  // zero encoder counters
  setMotorSpeed(MOTOR_L, speedL);
  setMotorSpeed(MOTOR_R, speedR);
  setMotorDirection(MOTOR_L, CLOCKWISE);
  setMotorDirection(MOTOR_R, COUNTER_CLOCKWISE);
}

void turnRightInPlace(int speed) {
  speedL = speed;
  speedR = speed;
  debounceTime = DEBOUNCETIME;
  resetEncoderCounters();  // zero encoder counters
  setMotorSpeed(MOTOR_L, speedL);
  setMotorSpeed(MOTOR_R, speedR);
  setMotorDirection(MOTOR_L, COUNTER_CLOCKWISE);
  setMotorDirection(MOTOR_R, CLOCKWISE);
}

Readings updateSpeed() {
  updateEncoderData();
  if (encoderCountL + encoderCountOldR > 5) {
    debounceTime = (50 * DEBOUNCETIME) / moveSpeed;
  }
  int difference = encoderCountR - encoderCountL;
  Serial.printf("BEFORE: EncoderL: %d, EncoderR: %d, Difference: %d, SpeedL: %d, SpeedR: %d\n", encoderCountL, encoderCountR, difference, speedL, speedR);
  int deltaSpeed = (moveSpeed * difference) / 6;
  // if (difference > 0) {
  speedL = moveSpeed + deltaSpeed;
  speedR = moveSpeed;
  if (speedL > 255) {
    speedR = moveSpeed - speedL + 255;
    speedL = 255;
    if (speedR < 0) {
      speedR = 0;
    }
  } else if (speedL < 0) {
    speedL = 0;
  }

  setMotorSpeed(MOTOR_L, speedL);
  setMotorSpeed(MOTOR_R, speedR);

  sprintf(eventString, "{\"type\":\"move\", \"time\":%d, \"encoderL\":%d, \"encoderR\":%d, \"speedL\":%d, \"speedR\":%d}", millis(), encoderCountL, encoderCountR, speedL, speedR);
  Readings result;
  result.type = ENCODER;
  result.readings[0] = encoderCountL;
  result.readings[1] = encoderCountR;
  return result;
}
