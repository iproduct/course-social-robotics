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

# Initialize a motor at port B.
test_motor = Motor(Port.A)

# Write your program here.
# ev3.speaker.beep()

# Run the motor up to 500 degrees per second. To a target angle of 90 degrees.
# test_motor.run_target(500, 650)
# test_motor.run_until_stalled(200, then=Stop.HOLD, duty_limit=150)
test_motor.run_angle(-100, 90)
# Play another beep sound.
ev3.speaker.beep(1000, 200)
