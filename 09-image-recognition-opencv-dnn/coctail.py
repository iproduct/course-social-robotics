import wiringpi
import argparse

global impulses1
global impulses2
global impulses3


def gpio_callback1():
    global impulses1
    impulses1 += 1
    print("pump1 %d" % impulses1)
    
def gpio_callback2():
    global impulses2
    impulses2 += 1
    print("pump2 %d" % impulses2)

def gpio_callback3():
    global impulses3
    impulses3 += 1
    print("pump3 %d" % impulses3)

def makeCoctail(quantities):
    q1 = quantities[0]
    q2 = quantities[1]
    q3 = quantities[2]

    print("quantities: %d %d %d" % (q1, q2, q3))

    global impulses1
    impulses1 = 0
    global impulses2
    impulses2 = 0
    global impulses3
    impulses3 = 0
    
    pump1 = 17
    pump2 = 21
    pump3 = 22

    meter1 = 25 
    meter2 = 24
    meter3 = 23

    wiringpi.wiringPiSetupGpio()
    wiringpi.pinMode(pump1, wiringpi.GPIO.OUTPUT)
    wiringpi.pinMode(pump2, wiringpi.GPIO.OUTPUT)
    wiringpi.pinMode(pump3, wiringpi.GPIO.OUTPUT)

    wiringpi.pinMode(meter1, wiringpi.GPIO.INPUT)
    wiringpi.pinMode(meter2, wiringpi.GPIO.INPUT)
    wiringpi.pinMode(meter3, wiringpi.GPIO.INPUT)


    wiringpi.pullUpDnControl(meter1, wiringpi.GPIO.PUD_UP)
    wiringpi.pullUpDnControl(meter2, wiringpi.GPIO.PUD_UP)
    wiringpi.pullUpDnControl(meter3, wiringpi.GPIO.PUD_UP)


    wiringpi.wiringPiISR(meter1, wiringpi.GPIO.INT_EDGE_BOTH , gpio_callback1)
    wiringpi.wiringPiISR(meter2, wiringpi.GPIO.INT_EDGE_BOTH , gpio_callback2)
    wiringpi.wiringPiISR(meter3, wiringpi.GPIO.INT_EDGE_BOTH , gpio_callback3)

    if q1 > 0:
        wiringpi.digitalWrite(pump1, 1)

    if q2 > 0:
        wiringpi.digitalWrite(pump2, 1)
        
    if q3 > 0:
        wiringpi.digitalWrite(pump3, 1) 

    flag1 = 0
    flag2 = 0
    flag3 = 0
    
    while flag1 == 0 or flag2 == 0 or flag3 == 0:
        ml1 = impulses1 * 0.085034
        if ml1 >= q1:
            wiringpi.digitalWrite(pump1, 0)
            flag1 = 1
            
        ml2 = impulses2 * 0.085034
        if ml2 >= q2:
            wiringpi.digitalWrite(pump2, 0)
            flag2 = 1
            
        ml3 = impulses3 * 0.085034
        if ml3 >= q3:
            wiringpi.digitalWrite(pump3, 0)
            flag3 = 1
                    
        wiringpi.delay(100)
        print("mililiters: %d %d %d" % (ml1, ml2, ml3))

    wiringpi.digitalWrite(pump1, 0)
    wiringpi.digitalWrite(pump2, 0)
    wiringpi.digitalWrite(pump3, 0)
    impulses1 = 0
    impulses2 = 0
    impulses3 = 0
    return (ml1, ml2, ml3)






