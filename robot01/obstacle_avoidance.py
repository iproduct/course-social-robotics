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

# Initialize a gripping motor at port A.
grip_motor = Motor(Port.A)

# Initialize two motors with default settings on Port B and Port C.
# These will be the left and right motors of the drive base.
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

# Initialize sensors
touch_sensor = TouchSensor(Port.S1)
color_sensor = ColorSensor(Port.S4)
infrared_sensor = InfraredSensor(Port.S3)

# The DriveBase is composed of two motors, with a wheel on each motor.
# The wheel_diameter and axle_track values are used to make the motors
# move at the correct speed when you give a motor command.
# The axle track is the distance between the points where the wheels
# touch the ground.
robot = DriveBase(left_motor, right_motor, wheel_diameter=32, axle_track=172)

# Play a sound to tell us when we are ready to start moving
ev3.speaker.beep()
ev3.speaker.set_speech_options('en', 'f1', 20, 90)
ev3.speaker.set_volume(100)
ev3.speaker.say('Hello from Lego')

# Run the grip motor up to 500 degrees per second. To a target angle of 360 degrees.
# grip_motor.run_target(500, 360)

# robot.straight(300)
# robot.turn(90)

# The following loop makes the robot drive forward until it detects an
# obstacle. Then it backs up and turns around. It keeps on doing this
# until you stop the program.
i = 0
while True:
    # Begin driving forward at 200 millimeters per second.
    robot.drive(100, 0)

    # Wait until an obstacle is detected. This is done by repeatedly
    # doing nothing (waiting for 10 milliseconds) while the measured
    # distance is still greater than 300 mm.
    while not touch_sensor.pressed():
        wait(10)
        if i%100 == 0:
            print(infrared_sensor.keypad())
        i += 1

    # Drive backward for 300 millimeters.
    robot.straight(-300)

    # Turn around by 120 degrees
    robot.turn(90)








# Run the motor up to 500 degrees per second. To a target angle of 90 degrees.
# grip_motor.run_target(500, 360)
# grip_motor.run_until_stalled(-500, then=Stop.HOLD, duty_limit=150)

# Play another beep sound.
ev3.speaker.beep(1000, 200)
