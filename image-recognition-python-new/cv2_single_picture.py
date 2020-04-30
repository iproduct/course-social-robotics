import cv2
from time import sleep
import argparse
import os

cap = cv2.VideoCapture(0)
print(cv2.__version__)

while True:
    # Capture frame-by-framehttps://github.com/iproduct/course-angular/wiki/Important-Dates
    ret, frame = cap.read()

    # Our operations on the frame come here
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Display the resulting frame
    cv2.imshow('frame', gray)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


path = os.getcwd() + "/images/image.jpg"
# Using cv2.imwrite() method
# Saving the image
cv2.imwrite(path, frame)

# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()