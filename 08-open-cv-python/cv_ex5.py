"""
cv_ex5.py - lines detection using Hough Transform

"""

import cv2
import numpy as np

if __name__ == "__main__":
    print(cv2.__version__)
    cap = cv2.VideoCapture(0)
    if(cap.isOpened()):
        print("webCam opened")
        while(cv2.waitKey(3) != ord('q')):
            ret, frame = cap.read()
            # Canny method for edges detection
            edges = cv2.Canny(frame,100,200)
            # Hough transform for finding lines
            # argument : edges_array,
            #            scaling for r in Hough Transform : 1 pixel
            #            scaling for thita in Hough Transform : 1 degree (3.14/180 in rad)
            #            threshold to define lines
            #            min line length (optional)
            #            max line gap (optional)
            lines = cv2.HoughLinesP(edges,1,np.pi/180,5,500,10)
            print(lines.shape)
            # drawing out the detected lines in green (0,255,0)
            # argument : (draw on)image_array, (from)point1, (to)point2
            if(lines is not None):
                for x1,y1,x2,y2 in lines[:,0,:] :
                    cv2.line(frame,(x1,y1),(x2,y2),(0,255,0),2)

            cv2.imshow("webCam",frame)
    else:
        print("Opening webCam failed")

    cap.release()
    cv2.destroyAllWindows()
