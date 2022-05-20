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
    cv.createTrackbar("Block Size", "Trackbars", 2, 5, empty)
    cv.createTrackbar("Appreture Size", "Trackbars", 3, 5, empty)
    cv.createTrackbar("Threshold", "Trackbars", 200, 500, empty)

    # open a video file
    cap = cv.VideoCapture(0)
    if cap.isOpened():

        while (cv.waitKey(20) & 0xFF != ord('q') and cv.waitKey(30) & 0xFF != 27):
            blockSize = cv.getTrackbarPos("Block Size", "Trackbars")
            appretureSize = cv.getTrackbarPos("Appreture Size", "Trackbars")
            threshold = cv.getTrackbarPos("Threshold", "Trackbars")

            # read image  frame
            success, img = cap.read()
            if not success:
                print("Video ended.")
                break
            img = cv.resize(img, (WIDTH, HEIGHT))
            # cv.imshow("Camera Capture", img)
            gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

            # Haris corner detection
            dst = cv.cornerHarris(gray, blockSize, appretureSize, 0.04)

            # Normalizing
            dst_norm = np.empty(dst.shape, dtype=np.float32)
            cv.normalize(dst, dst_norm, alpha=0, beta=255, norm_type=cv.NORM_MINMAX)

            # dst_norm_scaled = cv.convertScaleAbs(dst_norm)
            # dst_norm_scaled_bgr = cv.cvtColor(dst_norm_scaled, cv.COLOR_GRAY2BGR)
            # Drawing a circle around corners
            for i in range(dst_norm.shape[0]):
                for j in range(dst_norm.shape[1]):
                    if int(dst_norm[i, j]) > threshold:
                        cv.circle(img, (j, i), 5, (0, 255, 0), 2)
            # Showing the result
            cv.imshow("Corners Harris", img)

    else:
        print("Could capture web camera")

    cap.release()
    cv.destroyAllWindows()
