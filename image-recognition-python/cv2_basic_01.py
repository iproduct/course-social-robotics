# Importing the OpenCV library 
import cv2
from time import sleep
# Reading the image using imread() function 
image = cv2.imread('images/image.jpg') 

# Extracting the height and width of an image 
h, w = image.shape[:2] 
# Displaying the height and width 
print("Height = {}, Width = {}".format(h, w)) 
# Extracting RGB values. 
# Here we have randomly chosen a pixel 
# by passing in 100, 100 for height and width. 
(B, G, R) = image[100, 100] 

# Displaying the pixel values 
print("R = {}, G = {}, B = {}".format(R, G, B)) 

# We can also pass the channel to extract 
# the value for a specific channel 
B = image[100, 100, 2] 
print("R = {}".format(B))

roi = image[1000 : 1500, 500 : 1000]
resize = cv2.resize(image, (800, 800))
# Calculating the center of the image 
center = (w // 2, h // 2) 
  
# Generating a rotation matrix 
matrix = cv2.getRotationMatrix2D(center, -45, 1.0)  
  
# Performing the affine transformation 
rotated = cv2.warpAffine(image, matrix, (w, h))

# Copying the original image 
output = resize.copy() 
  
# Adding the text using putText() function 
text = cv2.putText(output, 'OpenCV Demo', (100, 550),  
                   cv2.FONT_HERSHEY_SIMPLEX, 4, (255, 0, 0), 2)

# Using the rectangle() function to create a rectangle. 
rectangle = cv2.rectangle(text, (300, 500),  
                          (700, 600), (0, 255, 0), 5) 


cv2.imshow('region', rectangle)

# sleep(5)

while True:
    # Capture frame-by-framehttps://github.com/iproduct/course-angular/wiki/Important-Dates
#     ret, frame = cap.read()

    # Our operations on the frame come here
#     gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Display the resulting frame
#     cv2.imshow('region', roi)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cv2.destroyAllWindows()
