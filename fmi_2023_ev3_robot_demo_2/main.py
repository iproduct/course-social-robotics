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
ev3.screen.draw_text(10,10, 'Hi')
ev3.speaker.set_speech_options('en', 'm1', 150, 50)
ev3.speaker.set_volume(100)
ev3.speaker.say('''I like to talk because I am a robot.
Did you know that robots like to make sounds?
Beep. Boop. Dit. Dit. Meep.
I am just such a chatterbox.''')
