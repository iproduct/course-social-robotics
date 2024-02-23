#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import (Motor, TouchSensor, ColorSensor,
                                 InfraredSensor, UltrasonicSensor, GyroSensor)
from pybricks.parameters import Port, Stop, Direction, Button, Color
from pybricks.tools import wait, StopWatch, DataLog
from pybricks.robotics import DriveBase
from pybricks.media.ev3dev import SoundFile, ImageFile


# This program requires LEGO EV3 MicroPython v2.0 or higher.
# Click "Open user guide" on the EV3 extension tab for more information.


# Create your objects here.
ev3 = EV3Brick()


# Write your program here.
ev3.speaker.beep()

# Initialize a gripping motor at port A
grip_motor = Motor(Port.A)

# Intialize two motors with default settings on Ports B and C
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

# Initilize sensors
touch_sensor = TouchSensor(Port.S1)
color_sensor = ColorSensor(Port.S4)
infrared_sensor = InfraredSensor(Port.S3)

# Initialize DriverBase
robot = DriveBase(left_motor, right_motor, wheel_diameter=32, axle_track=172)

# Open grip
# grip_motor.reset_angle(0)
# print(grip_motor.angle())
# grip_motor.run_target(500, -50)

# Robot speaks
ev3.speaker.set_speech_options('en', 'm1', 150, 50)
ev3.speaker.set_volume(100)
ev3.speaker.say('''I like to talk because I am a robot.
Did you know that robots like to make sounds?
Beep. Boop. Dit. Dit. Meep.
I am just such a chatterbox.''')

# Move robot
# robot.straight(300)
i = 0
turns = 0
while turns < 6:
    # Begin driving forward at 200 millimeters per second.
    robot.drive(100, 0)

    # Wait until an obstacle is detected. This is done by repeatedly
    # doing nothing (waiting for 10 milliseconds) while the measured
    # distance is still greater than 300 mm.
    while not touch_sensor.pressed():
        wait(10)
        if i%10 == 0:
            # print(infrared_sensor.keypad())
            buttons = infrared_sensor.buttons(1)
            print(buttons)
            if Button.BEACON in buttons:
                beacon = infrared_sensor.beacon(1)
                print(beacon)
        i += 1

    # Drive backward for 300 millimeters.
    robot.straight(-300)

    # Turn around by 90 degrees
    robot.turn(90)

    turns += 1