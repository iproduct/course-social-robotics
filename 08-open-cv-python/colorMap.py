"""
colorMap.py - BGR color map

"""

import numpy as np
import cv2

def nothing(x):
    pass

img = np.zeros((300,512,3), np.uint8)
cv2.namedWindow("color map")

cv2.createTrackbar("R","color map",0,255,nothing)
cv2.createTrackbar("G","color map",0,255,nothing)
cv2.createTrackbar("B","color map",0,255,nothing)

switch = "OFF/ON"
cv2.createTrackbar(switch,"color map",0,1,nothing)

while(1):
    cv2.imshow("color map",img)
    if cv2.waitKey(1) == ord('q'):
        break

    r= cv2.getTrackbarPos("R","color map")
    g= cv2.getTrackbarPos("G","color map")
    b= cv2.getTrackbarPos("B","color map")
    s= cv2.getTrackbarPos(switch,"color map")

    if s==0:
        img[:]=0
    else:
        img[:]=[b,g,r]


cv2.destroyAllWindows()
