# Python code to reading an image using OpenCV 
import numpy as np 
import cv2
import os 

# Image directory 
directory = os.getcwd() + '/images/'

# Image path 
image_path = directory + 'image.jpg'

try:   
    # Using cv2.imread() method 
    # to read the image 
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE) 
      
    # Change the current directory  
    # to specified directory  
    os.chdir(directory) 


    # You can give path to the 
    # image as first argument 
    # img = cv2.imread('images/image.jpg', 0)
    resized = cv2.resize(img, (1200, 800))
      
    # Canny edge detection. 
    edges = cv2.Canny(img, 50, 200) 

    # will show the image in a window 
    cv2.imshow('image', edges) 
    k = cv2.waitKey(0) & 0xFF

    # wait for ESC key to exit 
    if k == 27: 
        cv2.destroyAllWindows() 
        
    # wait for 's' key to save and exit 
    elif k == ord('s'):
      
        # List files and directories     
        print("Before saving image:")   
        print(os.listdir(directory))   
          
        # Filename 
        filename = 'result.jpg'
          
        # Using cv2.imwrite() method 
        # Saving the image 
        cv2.imwrite(filename, resized) 
          
        # List files and directories     
        print("After saving image:")   
        print(os.listdir(directory)) 
          
        print('Successfully saved') 
        cv2.destroyAllWindows()
    
except IOError: 
    print ('Error while reading files !!!') 