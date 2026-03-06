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

def is_on_red(rgb):
    return rgb[0] > (rgb[1] + rgb[2])
    # return rgb[2] < black_treshold and rgb[0] < black_treshold and rgb[1] < black_treshold


# Create your objects here.
ev3 = EV3Brick()


# # Write your program here.
# ev3.speaker.beep()

# # Robot speaks
# ev3.speaker.set_speech_options('en', 'f1', 150, 80)
# ev3.speaker.set_volume(100)
# ev3.speaker.say('''I like to talk because I am a robot.
# Did you know that robots like to make sounds?
# Beep. Boop. Dit. Dit. Meep.
# I am just such a chatterbox.''')

# Init gripper motor
grip_motor = Motor(Port.A)

# Init movement motors
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

# Test grip motor
grip_motor.reset_angle(0)
# print('Initial angle:', grip_motor.angle())
# grip_motor.run_until_stalled(500)
grip_motor.run_angle(500, 50)
print('Final angle:', grip_motor.angle())

# DriverBase init
robot = DriveBase(left_motor, right_motor, wheel_diameter = 32, axle_track = 185)

# Test robot movement
# robot.drive(100, 0)
# wait(5000)
# robot.stop()

# Initilize sensors
touch_sensor = TouchSensor(Port.S1)
color_sensor = ColorSensor(Port.S4)
infrared_sensor = InfraredSensor(Port.S3)
robot.stop()
# Move while not obstacle
i = 0
turns = 0
finish = False
while turns < 4 and not finish:
    # Begin driving forward at 100 millimeters per second.
    robot.drive(100, 0)

    # Wait until an obstacle is detected. This is done by repeatedly
    # doing nothing (waiting for 10 milliseconds) while the measured
    # distance is still greater than 300 mm.

    obstacle = touch_sensor.pressed()
    while not obstacle and not finish:
        rgb = color_sensor.rgb()
        if is_on_red(rgb):
            finish = True
        if i%10 == 0:
            print('RGB: ' + str(rgb))
        
        wait(10)
        i += 1
        obstacle = touch_sensor.pressed()

    # Drive backward for 300 millimeters.
    robot.straight(-300)
    # Turn around by 90 degrees
    robot.turn(90)
    turns += 1

robot.stop()


