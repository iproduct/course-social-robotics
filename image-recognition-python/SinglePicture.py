from picamera import PiCamera
from time import sleep
import argparse
import os

camera = PiCamera()

path = os.getcwd() + "/images/"

camera.start_preview()
sleep(3)
camera.capture(path + '/image.jpg')
camera.stop_preview()
