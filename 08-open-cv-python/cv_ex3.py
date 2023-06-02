"""
cv_ex3.py - using webCam 

"""

import cv2
if __name__ == "__main__":
    # set the argument to be 0
    # the index number is to specify which camera will be used
    cap = cv2.VideoCapture(0)

    if(cap.isOpened()):
        print('FaceTime')
        # press q to quit the webCam
        while(cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()
            cv2.imshow('webCam',frame)
        else:
            cv2.imwrite("picture.png", frame)
    else:
        print('webCam is failed to use')

    cap.release()
    cv2.destroyAllWindows()
