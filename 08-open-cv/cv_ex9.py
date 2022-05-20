"""
cv_ex9.py - face recognition test

"""

import cv2

cap = cv2.VideoCapture(0)

if(cap.isOpened()):
    print("webCam opened")
    # training model for face and eyes
    face = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    eyes = cv2.CascadeClassifier('haarcascade_eye.xml')
    
    while(cv2.waitKey(3) != ord('q')):
        ret, frame = cap.read()
        # convert to grayscale image
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # face detection 
        faceDetect = face.detectMultiScale(gray, 1.3, 5)
        # draw a bounding box for the detected face
        for (x,y,w,h) in faceDetect:
            frame = cv2.rectangle(frame,(x,y),(x+w,y+h),(255,0,0),2)
            
        cv2.imshow("webCam", frame)

else:
    print("Opening video failed")

cap.release()
cv2.destroyAllWindows()
