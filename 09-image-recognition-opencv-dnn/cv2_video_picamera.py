from picamera import PiCamera
from time import sleep
import argparse
import os

camera = PiCamera()

path = os.getcwd() + "/images/"

camera.start_preview()
#camera.capture(path + '/image.jpg')
camera.start_recording(path + 'video_29_04_2020_01.h264')
sleep(10)
camera.stop_recording()
camera.stop_preview()
