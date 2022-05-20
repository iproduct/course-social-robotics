import sys

import cv2 as cv
import numpy as np

WIDTH = 640
HEIGHT = 480

def empty(arg):
    pass

if __name__ == '__main__':

    # show trackbars window
    cv.namedWindow("Trackbars")
    cv.resizeWindow("Trackbars", 640, 80)
    cv.createTrackbar("Threshold Min", "Trackbars", 100, 300, empty)
    cv.createTrackbar("Threshold Max", "Trackbars", 200, 300, empty)

    # open a video file
    cap = cv.VideoCapture(0)
    if cap.isOpened():
        print(f"Video capture is opened.")
        kernel = np.ones((3,3), np.uint8)

        while (cv.waitKey(1) & 0xFF != ord('q') and cv.waitKey(30) & 0xFF != 27):
            lower = cv.getTrackbarPos("Threshold Min", "Trackbars")
            upper = cv.getTrackbarPos("Threshold Max", "Trackbars")
            # print(lower, upper)
            success, img = cap.read()
            if not success:
                print("Video ended.")
                break
            img = cv.resize(img, (WIDTH, HEIGHT))
            cv.imshow("Camera Capture", img)
            gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
            blurred = cv.GaussianBlur(img, (15,15), 10)
            cv.imshow("Camera Capture Gayscale", blurred)
            # Canny method for edges detection
            # argument: image_array, minVal, maxVal
            # above maxVal: sure-edges, below minVal : non-edges
            # between maxVal and minVal : depend on the connectivity to sure-edges
            edges = cv.Canny(gray, lower, upper)
            cv.imshow("Camera Capture Canny Edges", edges)
            dilated = cv.dilate(edges, kernel, iterations=3)
            cv.imshow("Edges Dilated", dilated)
            eroded = cv.erode(dilated, kernel, iterations=3)
            cv.imshow("Edges Eroded", eroded)

    else:
        print("Could capture web camera")

    cap.release()
    cv.destroyAllWindows()
