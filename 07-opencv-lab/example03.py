import cv2 as cv
import sys
import numpy as np

if __name__ == '__main__':
    print(f'OpenCV imported: {cv.version.opencv_version}')
    img = cv.imread("resources/cards.jpg")
    if img is None:
        sys.exit("Could not read the image.")
    print(img.shape)
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    print(gray.shape)

    blurred = cv.GaussianBlur(gray, (15, 15), 10)
    edges = cv.Canny(gray, 80, 200)
    kernel = np.ones((3, 3), np.uint8)
    dilated = cv.dilate(edges, kernel, iterations=1)
    eroded = cv.erode(dilated, kernel, iterations=1)

    cv.imshow("Image", gray)
    # cv.imshow("Lena [blurred]", blurred)
    cv.imshow("Image [edge detection]", edges)
    cv.imshow("Image [edge dilated]", dilated)
    cv.imshow("Image [edge dilated eroded]", eroded)

    cv.waitKey(30000)
    cv.destroyAllWindows()
