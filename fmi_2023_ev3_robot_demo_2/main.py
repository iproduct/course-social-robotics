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
ev3.screen.draw_text(10,10, 'Hi')
# ev3.speaker.set_speech_options('en', 'm1', 150, 50)
# ev3.speaker.set_volume(100)
# ev3.speaker.say('''I like to talk because I am a robot.
# Did you know that robots like to make sounds?
# Beep. Boop. Dit. Dit. Meep.
# I am just such a chatterbox.''')

# Run the grip motor up to 500 degrees per second. To a target angle of 360 degrees.
# grip_motor.run_target(500, 360)

# robot.straight(300)
# robot.turn(90)

# The following loop makes the robot drive forward until it detects an
# obstacle. Then it backs up and turns around. It keeps on doing this
# until you stop the program.
i = 0
turn_dir = 1
turn_angle = 3
turn_increment = 3
current_turn = 0
while True:
    # Begin driving forward at 100 millimeters per second.
    robot.drive(100, 0)

    # Wait until an obstacle is detected. This is done by repeatedly
    # doing nothing (waiting for 10 milliseconds) while the measured
    # distance is still greater than 300 mm.

    obstacle = touch_sensor.pressed()
    color = color_sensor.color()
    if i%10 == 0:
        print(color)

    while not obstacle and color == Color.RED:
        turn_dir = 1
        turn_angle = 3
        current_turn = 0
        obstacle = touch_sensor.pressed()
        color = color_sensor.color()
        # print(infrared_sensor.keypad())
        # buttons = infrared_sensor.buttons(1)
        # print(buttons)
        # if Button.BEACON in buttons:
        #     beacon = infrared_sensor.beacon(1)
        #     print(beacon)
        
    if color != Color.RED:
        if abs(current_turn) > turn_angle: 
            turn_dir = -turn_dir
            turn_angle += turn_increment
        robot.turn(turn_dir*turn_increment)
        current_turn += turn_dir*turn_increment
        
    elif obstacle:
         # Drive backward for 300 millimeters.
        robot.straight(-300)
        # Turn around by 90 degrees
        robot.turn(90)

    else:
        wait(10)
        
    i += 1

   



# Run the motor up to 500 degrees per second. To a target angle of 90 degrees.
# grip_motor.run_target(500, 360)
# grip_motor.run_until_stalled(-500, then=Stop.HOLD, duty_limit=150)

# Play another beep sound.
ev3.speaker.beep(1000, 200)
