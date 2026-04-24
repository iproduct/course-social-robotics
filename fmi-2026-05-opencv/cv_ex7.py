"""
cv_ex6.py - object tracking using color filtering and shapes recognition

"""

import cv2
import numpy as np

if __name__ == "__main__":
    cap = cv2.VideoCapture(0)
    if(cap.isOpened()):
        print("webCam opened")
        while(cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()
            # convert color space from BGR to HSV
            hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
            # set up lower and upper bound for color range of blue
            lowBound = np.array([105,50,50])
            upBound = np.array([130,255,255])
            # filtering by color
            mask = cv2.inRange(hsv,lowBound,upBound)
            # image dilation
            kernel = np.ones((5,5),np.uint8)
            mask = cv2.dilate(mask,kernel,iterations = 1)
            # contour extraction
            cnts, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
            # show 2 objects (or more, depend on your setting)
            cnts = sorted(cnts, key = cv2.contourArea, reverse = True)[:2] # 2
            for c in cnts:
                peri = cv2.arcLength(c,True)
                approx = cv2.approxPolyDP(c, 0.02*peri, True)
                if (len(approx)==4): # if it is 4-sides polygon
                    cv2.drawContours(frame,[approx],0,(0,255,0),-1)
                if (len(approx)>10): # if the polygon is more than 10 sides (may be circles)
                    cv2.drawContours(frame,[approx],0,(0,0,255),-1)

            # combined two images (filter_image,original_image) in one window
            h1,w1 = mask.shape
            h2,w2,d2 = frame.shape
            imgCom = np.zeros((max([h1,h2]),w1+w2,3), dtype='uint8')
            imgCom[:h1,:w1,0] = mask
            imgCom[:h1,:w1,1] = mask
            imgCom[:h1,:w1,2] = mask
            imgCom[:h2,w1:w1+w2,:] = np.dstack([frame])
            # image resize
            # imgCom = cv2.resize(imgCom,(w1,h1),interpolation=cv2.INTER_LINEAR)
            cv2.imshow("webCam",imgCom)
    else:
        print("Opening video failed")
    cap.release()
    cv2.destroyAllWindows()
