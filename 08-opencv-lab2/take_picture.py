import sys

import cv2 as cv
import numpy as np

WIDTH = 640
HEIGHT = 480

def empty(arg):
    pass

if __name__ == '__main__':
    # open a video file
    cap = cv.VideoCapture(0)
    if cap.isOpened():
        print(f"Video captured successfully.")
        success, img = cap.read()
        img = cv.resize(img, (WIDTH, HEIGHT))
        cv.imshow("Camera Capture", img)
        cv.imwrite("resources/book_cover.png", img)
        cv.waitKey(15000)

    else:
        print("Could capture web camera")

    cap.release()
    cv.destroyAllWindows()
