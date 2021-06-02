from picamera import PiCamera
from time import sleep
import argparse
import os

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-n", "--name", required=True,
	help="path to input person's name")
ap.add_argument("-s", "--start", type=int, default=0,
	help="image start index")
ap.add_argument("-c", "--count", type=int, default=5,
	help="count of images")
args = vars(ap.parse_args())
name = args["name"]
start = args["start"]
count = args["count"]

camera = PiCamera()

path = os.getcwd() + "/dataset/" + name

try:
    if not os.path.isdir(path):
        os.mkdir(path)
    
except OSError:  
    print ("Creation of the directory %s failed" % path)
else:  
    print ("Successfully created the directory %s" % path)
    
    #fullscreen=False,window=(100,200,300,400)
    camera.start_preview()
    for i in range(start, start + count):
        sleep(2)
        camera.capture(path + '/image%s.jpg' % i)
    camera.stop_preview()
