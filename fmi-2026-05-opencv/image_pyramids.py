# Image Pyramids (Gaussian & Laplacian)

import cv2

# Load image
image = cv2.imread(r'data\astronaut_s.jpg')

# Gaussian Pyramid (Downsampling)
lower_res = cv2.pyrDown(image)  # Reduces resolution by half
lower_res_again = cv2.pyrDown(lower_res)  # Even smaller

# Laplacian Pyramid (Edge highlighting)
# Requires Gaussian pyramid first
higher_res = cv2.pyrUp(lower_res)  # Approximates original size (blurry)
laplacian = cv2.subtract(image, higher_res)  # Highlights edges

# Display results
cv2.imshow("Original", image)
cv2.imshow("Gaussian Pyramid 1", lower_res)
cv2.imshow("Gaussian Pyramid 2", lower_res_again)
cv2.imshow("Laplacian Pyramid", laplacian)

cv2.waitKey(0)
cv2.destroyAllWindows()