from picamera import PiCamera, Color
from time import sleep
import argparse
import os

camera = PiCamera()
# camera.resolution = (2592, 1944)
camera.framerate = 15
camera.annotate_text_size = 100
camera.annotate_background = Color('green')
camera.annotate_foreground = Color('white')
# camera.annotate_text = "Hello world!"

path = os.getcwd() + "/images/temp/"

camera.start_preview()
for effect in camera.IMAGE_EFFECTS:
    camera.image_effect = effect
    camera.annotate_text = "Effect: %s" % effect
    sleep(0.1)
    camera.capture(path + 'image_effect%s.jpg' % effect)
# for i in range(5):
#     sleep(5)
#     camera.capture('/home/pi/Desktop/image%s.jpg' % i)
camera.stop_preview()
