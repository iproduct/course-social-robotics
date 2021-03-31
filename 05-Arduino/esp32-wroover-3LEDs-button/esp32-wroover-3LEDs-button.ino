int ledpinR = 32; //define the LED connect to Pin11;
int ledpinG = 33; //define the LED connect to Pin11;
int ledpinY = 25; //define the LED connect to Pin11;
int inpin = 34; //define the button connect to Pin7
const unsigned long LED_DURATION = 200;


int val;//val to store the data
int activeLed = -1; // 0 - 2
unsigned long switchonTime;

void setup()
{
  pinMode(ledpinR, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(ledpinG, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(ledpinY, OUTPUT); //define the LED Pin to OUTPUT;
  pinMode(inpin, INPUT); //define the button Pin to INPUT;
}
void loop()
{
  val = digitalRead(inpin); //digital read the button pin
  if (val == LOW) //if the button pressed;
  {
    trunLedsOff();
    activeLed = -1;
  }
  else
  {
    if (activeLed == -1) {
      switchonTime = millis();
      activeLed = 0;
      turnLedOn(activeLed);
    }
    if (millis() - switchonTime > LED_DURATION) {
      switchonTime = millis();
      activeLed = (activeLed + 1) % 3;
      trunLedsOff();
      turnLedOn(activeLed);
    }
    delay(20);
  }
}

void trunLedsOff() {
  digitalWrite(ledpinR, LOW); //turn off the LED
  digitalWrite(ledpinG, LOW); //turn off the LED
  digitalWrite(ledpinY, LOW); //turn off the LED
}

void turnLedOn(int activeLed) {
  switch(activeLed) {
  case 0: digitalWrite(ledpinR, HIGH);  break;
  case 1: digitalWrite(ledpinG, HIGH); break;
  case 2: digitalWrite(ledpinY, HIGH); break;
  }
}
