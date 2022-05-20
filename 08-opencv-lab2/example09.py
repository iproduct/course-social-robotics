"""
cv_ex9.py - face recognition test

"""

import cv2
import cv2.data

if __name__ == "__main__":
    cap = cv2.VideoCapture(0)

    if (cap.isOpened()):
        print("webCam opened")
        # training model for face and eyes
        faceCascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
        eyesCascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

        while (cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()
            # convert to grayscale image
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            # face detection
            faceDetect = faceCascade.detectMultiScale(gray, 1.3, 5, minSize=(20, 20))
            # draw a bounding box for the detected face
            for (x, y, w, h) in faceDetect:
                frame = cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0), 2)
                roi_color = frame[y:y + h, x: x + w]
                roi_gray = frame[y:y + h, x: x + w]
                eyes = eyesCascade.detectMultiScale(roi_gray, scaleFactor=1.1, minNeighbors=5,
                                                    maxSize=(50, 40), flags=cv2.CASCADE_SCALE_IMAGE)
                for(ex, ey, ew, eh) in eyes[:2]:
                    cv2.rectangle(roi_color, (ex, ey), (ex + ew, ey + eh), (0, 255, 0), 2)

            cv2.imshow("webCam", frame)

    else:
        print("Opening video failed")

    cap.release()
    cv2.destroyAllWindows()
