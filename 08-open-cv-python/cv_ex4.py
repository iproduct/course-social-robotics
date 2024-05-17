"""
cv_ex4.py - edges detection using Canny method

"""

import cv2
import numpy as np
if __name__ == "__main__":
    cap = cv2.VideoCapture(0)

    if(cap.isOpened()):
        print("webCam opened")
        while(cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()

            # Canny method for edges detection
            # argument: image_array, minVal, maxVal
            # above maxVal: sure-edges, below minVal : non-edges
            # between maxVal and minVal : depend on the connectivity to sure-edges
            edges = cv2.Canny(frame,70,250)

            cv2.imshow('edges',edges)

    else:
        print("Opening webCam failed")

    cap.release()
    cv2.destroyAllWindows()
