"""
cv_ex8.py - ORB + BruteForceMatcher

"""

import numpy as np
import cv2
import math
if __name__ == "__main__":
    drawMatchingPoints = True # show Matching Points or not
    threshold = 0.08 # threshold for detection
    numPoints = 100 # number of points selected (best matching)

    img1 = cv2.imread('picture.png') # target image
    h1,w1,d1 = img1.shape
    corners1 = np.float32([ [0,0],[0,h1-1],[w1-1,h1-1],[w1-1,0] ]).reshape(-1,1,2)
    # detector
    orb = cv2.ORB_create()
    kp1, des1 = orb.detectAndCompute(img1,None)

    cap = cv2.VideoCapture(0); # access webCam
    while(True):
        ret, frame = cap.read()
        #img2 = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        img2 = frame # scene from webCam
        h2,w2,d2 = img2.shape

        # find keypoints and descriptors
        kp2, des2 = orb.detectAndCompute(img2,None)

        # match
        bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck = True)
        matches = bf.match(des1,des2)
        #print(len(matches))

        matches = sorted(matches, key = lambda x:x.distance)
        good = matches[:numPoints] # select the best "numPOints" points

        # image that combined img1 and img2
        imgCom = np.zeros((max([h1,h2]),w1+w2,3), dtype='uint8')
        imgCom[:h1,:w1,:] = np.dstack([img1])
        imgCom[:h2,w1:w1+w2,:] = np.dstack([img2])

        # corners' matching
        pts1 = np.float32([kp1[m.queryIdx].pt for m in good])
        pts2 = np.float32([kp2[m.trainIdx].pt for m in good])

        if drawMatchingPoints:
            # draw Matching Points
            for i in range(len(good)):
                (x1,y1)=pts1[i]
                (x2,y2)=pts2[i]
                # draw circle
                cv2.circle(imgCom, (int(x1),int(y1)), 4, (255, 0, 0), 1)
                cv2.circle(imgCom, (int(x2)+w1,int(y2)), 4, (255, 0, 0), 1)
                #draw line between (x1,y1) and (x2,y2)
                color = (np.random.randint(0,255),np.random.randint(0,255),np.random.randint(0,255))
                cv2.line(imgCom, (int(x1),int(y1)), (int(x2)+w1,int(y2)), color, 1)

        pts1 = pts1.reshape(-1,1,2)
        pts2 = pts2.reshape(-1,1,2)

        M,mask = cv2.findHomography(pts1,pts2,cv2.RANSAC)

        corners2 = cv2.perspectiveTransform(corners1,M)

        # vector1 = pt1-->pt4
        vect1=(corners2[3][0][0]-corners2[0][0][0],corners2[3][0][1]-corners2[0][0][1])
        # vector2 = pt1-->pt2
        vect2=(corners2[1][0][0]-corners2[0][0][0],corners2[1][0][1]-corners2[0][0][1])
        # check orthogonality of vector1 and vector2
        cosThita=np.abs(vect1[0]*vect2[0]+vect1[1]*vect2[1])/(math.sqrt(vect1[0]**2+vect1[1]**2)*math.sqrt(vect2[0]**2+vect2[1]**2))

        #print(cosThita)

        if cosThita < threshold:
            # shift 'w'
            corners2[0][0][0] = corners2[0][0][0]+w1
            corners2[1][0][0] = corners2[1][0][0]+w1
            corners2[2][0][0] = corners2[2][0][0]+w1
            corners2[3][0][0] = corners2[3][0][0]+w1

            # draw four lines to coordinat the target
            cv2.polylines(imgCom,[np.int32(corners2)],True,255,3)


        # imshow
        cv2.imshow('Matched Features',imgCom)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()
