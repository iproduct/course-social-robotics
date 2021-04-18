int ledpin=32;//define the LED connect to Pin11;
int inpin=34;//define the button connect to Pin7
int val;//val to store the data
void setup()
{
pinMode(ledpin,OUTPUT);//define the LED Pin to OUTPUT;
pinMode(inpin,INPUT);//define the button Pin to INPUT;
}
void loop()
{
val=digitalRead(inpin);//digital read the button pin
if(val==LOW)//if the button pressed;
{ digitalWrite(ledpin,LOW);}//turn off the LED
else
{ digitalWrite(ledpin,HIGH);}//if not pressed, turn on the LED
}
