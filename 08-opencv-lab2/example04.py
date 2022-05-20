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
    cv.resizeWindow("Trackbars", 640, 200)
    cv.createTrackbar("Threshold Min", "Trackbars", 100, 300, empty)
    cv.createTrackbar("Threshold Max", "Trackbars", 200, 300, empty)
    cv.createTrackbar("Threshold Hough Transform", "Trackbars", 100, 300, empty)
    cv.createTrackbar("Min Line Length Hough Transform", "Trackbars", 100, 300, empty)
    cv.createTrackbar("Max Line Gap Hough Transform", "Trackbars", 10, 300, empty)

    # open a video file
    cap = cv.VideoCapture(0)
    if cap.isOpened():
        print(f"Video capture is opened.")
        kernel = np.ones((3, 3), np.uint8)
        print(kernel)

        while (cv.waitKey(1) & 0xFF != ord('q') and cv.waitKey(30) & 0xFF != 27):
            lower = cv.getTrackbarPos("Threshold Min", "Trackbars")
            upper = cv.getTrackbarPos("Threshold Max", "Trackbars")
            threshold = cv.getTrackbarPos("Threshold Hough Transform", "Trackbars")
            length = cv.getTrackbarPos("Min Line Length Hough Transform", "Trackbars")
            gap = cv.getTrackbarPos("Max Line Gap Hough Transform", "Trackbars")
            # print(lower, upper)
            success, img = cap.read()
            if not success:
                print("Video ended.")
                break
            img = cv.resize(img, (WIDTH, HEIGHT))
            # cv.imshow("Camera Capture", img)
            gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
            # cv.imshow("Camera Capture Gayscale", gray)
            # Canny method for edges detection
            # argument: image_array, minVal, maxVal
            # above maxVal: sure-edges, below minVal : non-edges
            # between maxVal and minVal : depend on the connectivity to sure-edges
            edges = cv.Canny(gray, lower, upper)
            cv.imshow("Camera Capture Canny Edges", edges)

            # Hough transform for finding lines
            # argument : edges_array,
            #            scaling for r in Hough Transform : 1 pixel
            #            scaling for thita in Hough Transform : 1 degree (3.14/180 in rad)
            #            threshold to define lines
            #            min line length (optional)
            #            max line gap (optional)
            lines = cv.HoughLinesP(edges, 1, np.pi / 180, threshold, length, gap)
            print(lines)
            # drawing out the detected lines in green (0,255,0)
            # argument : (draw on)image_array, (from)point1, (to)point2
            if (lines is not None):
                for x1, y1, x2, y2 in lines[:, 0, :]:
                    cv.line(img, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv.imshow("Camera Capture Hough Lines", img)

    else:
        print("Could capture web camera")

    cap.release()
    cv.destroyAllWindows()
