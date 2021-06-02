# Python code to reading an image using OpenCV 
import numpy as np 
import cv2
import os 

if __name__ == '__main__':
    # Init video capture
    cap = cv2.VideoCapture(0)
    if cap is None or not cap.isOpened():
        print('Error: unable to open video source: ', cap)
        exit(0)

    while True:
        # Capture frame-by-framehttps://github.com/iproduct/course-angular/wiki/Important-Dates
        ret, frame = cap.read()

        resized = cv2.resize(frame, (1200, 800))

        # Our operations on the frame come here
        gray = cv2.cvtColor(resized, cv2.COLOR_BGR2GRAY)

        # Canny edge detection.
        edges = cv2.Canny(gray, 50, 200)


        # Display the resulting frame
        cv2.imshow('frame', edges)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()
