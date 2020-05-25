# Python code to reading an image using OpenCV 
import numpy as np 
import cv2
import os 

# Init video capture
cap = cv2.VideoCapture(0)
if cap is None or not cap.isOpened():
    print('Error: unable to open video source: ', source)
    exit(0)

while True:
    # Capture frame-by-framehttps://github.com/iproduct/course-angular/wiki/Important-Dates
    ret, frame = cap.read()
    
    resized = cv2.resize(frame, (300, 200))

    # Our operations on the frame come here
    gray = cv2.cvtColor(resized, cv2.COLOR_BGR2GRAY)
          
    # applying different thresholding  
    # techniques on the input image 
    # all pixels value above 120 will  
    # be set to 255 
    ret, thresh1 = cv2.threshold(gray, 120, 255, cv2.THRESH_BINARY) 
    ret, thresh2 = cv2.threshold(gray, 120, 255, cv2.THRESH_BINARY_INV) 
    ret, thresh3 = cv2.threshold(gray, 120, 255, cv2.THRESH_TRUNC) 
    ret, thresh4 = cv2.threshold(gray, 120, 255, cv2.THRESH_TOZERO) 
    ret, thresh5 = cv2.threshold(gray, 120, 255, cv2.THRESH_TOZERO_INV)
    # applying different thresholding techniques on the input image 
    thresh6 = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, 
                                              cv2.THRESH_BINARY, 41, 5) 
      
    thresh7 = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, 
                                              cv2.THRESH_BINARY, 41, 5)
    # applying Otsu thresholding as an extra flag in binary thresholding      
    ret, thresh8 = cv2.threshold(gray, 60, 255, cv2.THRESH_BINARY + 
                                            cv2.THRESH_OTSU)    
      
    # the window showing output images 
    # with the corresponding thresholding  
    # techniques applied to the input images 
    cv2.imshow('Binary Threshold', thresh1) 
    cv2.imshow('Binary Threshold Inverted', thresh2) 
    cv2.imshow('Truncated Threshold', thresh3) 
    cv2.imshow('Set to 0', thresh4) 
    cv2.imshow('Set to 0 Inverted', thresh5)
    cv2.imshow('Adaptive Mean', thresh6) 
    cv2.imshow('Adaptive Gaussian', thresh7)
    cv2.imshow('Otsu Threshold', thresh8)
    

    # Display the resulting frame
    cv2.imshow('original', resized)
    
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    
cap.release()
cv2.destroyAllWindows() 
