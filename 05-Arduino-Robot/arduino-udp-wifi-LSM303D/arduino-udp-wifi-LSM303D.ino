#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <Wire.h>
//#include <LSM303.h>
//#include <L3G.h>

// WiFi connection data
const char* ssid = "your-wifi-network-ssid";
const char* password = "your-wifi-network-password";
const char* udp_server_ip = "192.168.137.1";
unsigned int udp_server_port = 4210;

WiFiUDP Udp;
unsigned int localUdpPort = 4210;  // local port to listen on
char incomingPacket[255];  // buffer for incoming packets

const int  ButtonPin = D6;    // the pin that the pushbutton is attached to

//Ultrasound sensor SNS-US020
unsigned int EchoPin = D5;           // connect Pin 4(Arduino digital io) to Echo at US-015
unsigned int TrigPin = D3;           // connect Pin 3(Arduino digital io) to Trig at US-015
unsigned long Time_Echo_us = 0;

// Uncomment the following line to use a MinIMU-9 v5 or AltIMU-10 v5. Leave commented for older IMUs (up through v4).
//#define IMU_V5

// Uncomment the below line to use this axis definition:
   // X axis pointing forward
   // Y axis pointing to the right
   // and Z axis pointing down.
// Positive pitch : nose up
// Positive roll : right wing down
// Positive yaw : clockwise
int SENSOR_SIGN[9] = {1,1,1,-1,-1,-1,1,1,1}; //Correct directions x,y,z - gyro, accelerometer, magnetometer
// Uncomment the below line to use this axis definition:
   // X axis pointing forward
   // Y axis pointing to the left
   // and Z axis pointing up.
// Positive pitch : nose down
// Positive roll : right wing down
// Positive yaw : counterclockwise
//int SENSOR_SIGN[9] = {1,-1,-1,-1,1,1,1,-1,-1}; //Correct directions x,y,z - gyro, accelerometer, magnetometer

// tested with Arduino Uno with ATmega328 and Arduino Duemilanove with ATMega168

// accelerometer: 8 g sensitivity
// 3.9 mg/digit; 1 g = 256
#define GRAVITY 256  //this equivalent to 1G in the raw data coming from the accelerometer

#define ToRad(x) ((x)*0.01745329252)  // *pi/180
#define ToDeg(x) ((x)*57.2957795131)  // *180/pi

// gyro: 2000 dps full scale
// 70 mdps/digit; 1 dps = 0.07
#define Gyro_Gain_X 0.07 //X axis Gyro gain
#define Gyro_Gain_Y 0.07 //Y axis Gyro gain
#define Gyro_Gain_Z 0.07 //Z axis Gyro gain
#define Gyro_Scaled_X(x) ((x)*ToRad(Gyro_Gain_X)) //Return the scaled ADC raw data of the gyro in radians for second
#define Gyro_Scaled_Y(x) ((x)*ToRad(Gyro_Gain_Y)) //Return the scaled ADC raw data of the gyro in radians for second
#define Gyro_Scaled_Z(x) ((x)*ToRad(Gyro_Gain_Z)) //Return the scaled ADC raw data of the gyro in radians for second

// LSM303/LIS3MDL magnetometer calibration constants; use the Calibrate example from
// the Pololu LSM303 or LIS3MDL library to find the right values for your board

#define M_X_MIN -1000
#define M_Y_MIN -1000
#define M_Z_MIN -1000
#define M_X_MAX +1000
#define M_Y_MAX +1000
#define M_Z_MAX +1000

#define Kp_ROLLPITCH 0.02
#define Ki_ROLLPITCH 0.00002
#define Kp_YAW 1.2
#define Ki_YAW 0.00002

/*For debugging purposes*/
//OUTPUTMODE=1 will print the corrected data,
//OUTPUTMODE=0 will print uncorrected data of the gyros (with drift)
#define OUTPUTMODE 1

#define PRINT_DCM 0     //Will print the whole direction cosine matrix
#define PRINT_ANALOGS 0 //Will print the analog raw data
#define PRINT_EULER 1   //Will print the Euler angles Roll, Pitch and Yaw

float G_Dt=0.02;    // Integration time (DCM algorithm)  We will run the integration loop at 50Hz if possible

long timer=0;   //general purpuse timer
long timer_old;
long timer24=0; //Second timer used to print values
int AN[6]; //array that stores the gyro and accelerometer data
int AN_OFFSET[6]={0,0,0,0,0,0}; //Array that stores the Offset of the sensors

int gyro_x;
int gyro_y;
int gyro_z;
int accel_x;
int accel_y;
int accel_z;
int magnetom_x;
int magnetom_y;
int magnetom_z;
float c_magnetom_x;
float c_magnetom_y;
float c_magnetom_z;
float MAG_Heading;

float Accel_Vector[3]= {0,0,0}; //Store the acceleration in a vector
float Gyro_Vector[3]= {0,0,0};//Store the gyros turn rate in a vector
float Omega_Vector[3]= {0,0,0}; //Corrected Gyro_Vector data
float Omega_P[3]= {0,0,0};//Omega Proportional correction
float Omega_I[3]= {0,0,0};//Omega Integrator
float Omega[3]= {0,0,0};

// Euler angles
float roll;
float pitch;
float yaw;

float errorRollPitch[3]= {0,0,0};
float errorYaw[3]= {0,0,0};

unsigned int counter=0;
byte gyro_sat=0;

float DCM_Matrix[3][3]= {
  {
    1,0,0  }
  ,{
    0,1,0  }
  ,{
    0,0,1  }
};
float Update_Matrix[3][3]={{0,1,2},{3,4,5},{6,7,8}}; //Gyros here


float Temporary_Matrix[3][3]={
  {
    0,0,0  }
  ,{
    0,0,0  }
  ,{
    0,0,0  }
};

//LSM303 compass;
//L3G gyro;

//float velocity[3] = {0, 0, 0}; // estimation of velocity vector
//float pos[3] = {0, 0, 0}; // 3D position estimation

char report[256];
int distance = -1;


void setup()
{
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  Serial.println();

  pinMode(ButtonPin, INPUT_PULLUP);        // initialize the button pin as a input
  pinMode(BUILTIN_LED, OUTPUT);     // Initialize the BUILTIN_LED pin as an output

  //Initialize ultrasound sensor
  pinMode(EchoPin, INPUT);                    //Set EchoPin as input, to receive measure result from US-015
  pinMode(TrigPin, OUTPUT);                   //Set TrigPin as output, used to send high pusle to trig measurement (>10us)
    
  // setup I2C
  I2C_Init();
  digitalWrite(BUILTIN_LED,LOW);    // LED ON

  Serial.printf("Connecting to %s ", ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println(" connected");

  Udp.begin(localUdpPort);
  Serial.printf("Now listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), localUdpPort);

  // setup LSM303D
  Accel_Init();
  Compass_Init();
  Gyro_Init();

  Serial.println("Callibrating Pololu MinIMU-9 + Arduino AHRS");
  for(int i=0;i<32;i++) {    // We take some readings...
    Read_Gyro();
    Read_Accel();
    for(int y=0; y<6; y++)   // Cumulate values
      AN_OFFSET[y] += AN[y];
    delay(20);
  }

  for(int y=0; y<6; y++)
    AN_OFFSET[y] = AN_OFFSET[y]/32;

  AN_OFFSET[5]-=GRAVITY*SENSOR_SIGN[5];

  Serial.println("Offsets:");
  for(int y=0; y<6; y++)
    Serial.println(AN_OFFSET[y]);

  digitalWrite(BUILTIN_LED,HIGH);   // LED OFF

  timer=millis();
  delay(20);
  counter=0;

//  compass.init();
//  compass.enableDefault();
//
//  // setup L3G
//  if (!gyro.init())
//  {
//    Serial.println("Failed to autodetect gyro type!");
//  }
//  gyro.enableDefault();
}


void loop()
{
//  int packetSize = Udp.parsePacket();
//  if (packetSize)
//  {
//    // receive incoming UDP packets
//    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
//    int len = Udp.read(incomingPacket, 255);
//    if (len > 0)
//    {
//      incomingPacket[len] = 0;
//    }
//    Serial.printf("UDP packet contents: %s\n", incomingPacket);
//  compass.read();
//  gyro.read();
//snprintf(report, sizeof(report), "T:%12d   A: %6d %6d %6d   M: %6d %6d %6d   G: %6d %6d %6d   B: %1d   D: %5d",
//    now,
//    compass.a.x, compass.a.y, compass.a.z,
//    compass.m.x, compass.m.y, compass.m.z,
//    gyro.g.x, gyro.g.y, gyro.g.z,
//    buttonState, distance);

  if((millis()-timer)>=20)  // Main loop runs at 50Hz
  {
    counter++;
    timer_old = timer;
    timer=millis();
    if (timer>timer_old)
    {
      G_Dt = (timer-timer_old)/1000.0;    // Real time of loop run. We use this on the DCM algorithm (gyro integration time)
      if (G_Dt > 0.2) {
         Serial.print("Long interval:");
         Serial.println(G_Dt);
         G_Dt = 0; // ignore integration times over 200 ms
      }
    }
    else
      G_Dt = 0;
  
    // *** DCM algorithm
    // Data adquisition
    Read_Gyro();   // This read gyro data
    Read_Accel();     // Read I2C accelerometer
      
    if (counter % 5 == 0)  // Read compass data at 10Hz... (5 loop runs)
    {
      Read_Compass();    // Read I2C magnetometer
      Compass_Heading(); // Calculate magnetic heading
    }

    // Calculations...
    Matrix_update();
    Normalize();
    Drift_correction();
    Euler_angles();
    // ***
    
    if (counter % 5 == 0) { // Read compass data at 10Hz... (5 loop runs)
      // read button state
      int buttonState = digitalRead(ButtonPin);
      
      // US-020 - read US sensor data
      digitalWrite(TrigPin, HIGH);              //begin to send a high pulse, then US-015 begin to measure the distance
      delayMicroseconds(50);                    //set this high pulse width as 50us (>10us)
      digitalWrite(TrigPin, LOW);               //end this high pulse
      Time_Echo_us = pulseIn(EchoPin, HIGH);               //calculate the pulse width at EchoPin, 
      if((Time_Echo_us < 60000) && (Time_Echo_us > 1))     //a valid pulse width should be between (1, 60000).
      {
        distance = (Time_Echo_us*34/100)/20;      //calculate the distance by pulse width, Len_mm = (Time_Echo_us * 0.34mm/us) / 20 (cm)
      }
      int len = snprintf(report, sizeof(report), "T: %10d   R: %5.2f   P: %5.2f    Y: %5.2f   A: %6d %6d %6d   G: %6d %6d %6d   M: %7d %7d %7d   B: %1d   D: %5d",
      timer,
      ToDeg(roll), ToDeg(pitch), ToDeg(yaw),
      accel_x, accel_y, accel_z - GRAVITY,
      gyro_x,  gyro_y, gyro_z,
      magnetom_x, magnetom_y, magnetom_z,
      1-buttonState, distance);
      Serial.println(report);
    
      // send back a reply, to the IP address and port we got the packet from
      int result = Udp.beginPacket(udp_server_ip, udp_server_port);
      if(result > 0) {
        Udp.write(report,len);
        Udp.endPacket();
      }
    }
  }
}
