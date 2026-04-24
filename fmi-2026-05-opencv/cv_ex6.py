"""
cv_ex6.py - object tracking using color filtering

"""

import cv2
import numpy as np

def nothing(x):
    pass

if __name__ == "__main__":
    cap = cv2.VideoCapture(0)
    cv2.namedWindow("video")
    cv2.createTrackbar("Min", "video", 105, 255, nothing)
    cv2.createTrackbar("Max", "video", 140, 255, nothing)

    if(cap.isOpened()):
        print("webCam opened")
        while(cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()

            # get min and max thresholds from trackbars
            minThr = cv2.getTrackbarPos("Min", "video")
            maxThr = cv2.getTrackbarPos("Max", "video")

            # convert color space from BGR to HSV
            hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
            # set up lower and upper bound for color range of blue
            lowBound = np.array([minThr,50,50])
            upBound = np.array([maxThr,255,255])
            # filtering by color
            mask = cv2.inRange(hsv,lowBound,upBound)
            # image dilation
            kernel = np.ones((5,5),np.uint8)
            mask = cv2.dilate(mask,kernel,iterations = 1)
            # combined two images (filter_image,original_image) in one window
            # print(mask.shape)
            # print(frame.shape)
            h1,w1 = mask.shape
            h2,w2,d2 = frame.shape
            imgCom = np.zeros((max([h1,h2]),w1+w2,3), dtype='uint8')
            imgCom[:h1,:w1,0] = mask
            imgCom[:h1,:w1,1] = mask
            imgCom[:h1,:w1,2] = mask
            imgCom[:h2,w1:w1+w2,:] = np.dstack([frame])
            # image resize
            # imgCom = cv2.resize(imgCom,(w1,h1),interpolation=cv2.INTER_LINEAR)
            cv2.imshow("video",imgCom)
    else:
        print("Opening video failed")
    cap.release()
    cv2.destroyAllWindows()
